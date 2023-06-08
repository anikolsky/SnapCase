package com.omtorney.snapcase.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.omtorney.snapcase.common.domain.Repository
import com.omtorney.snapcase.common.domain.model.Case
import com.omtorney.snapcase.common.domain.model.ProcessStep
import com.omtorney.snapcase.common.domain.usecase.UseCases
import com.omtorney.snapcase.common.presentation.logd
import com.omtorney.snapcase.common.util.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class CaseCheckWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val useCases: UseCases,
    private val repository: Repository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        logd("Starting work")
        return try {
            val favorites = useCases.getFavoriteCases().first()
            logd("Iterating through favorites (size is ${favorites.size})")
            favorites.forEach { savedCase ->
                val document = repository.getJsoupDocument(savedCase.url)
                val appeal = mutableMapOf<String, String>()
                val processElement = document?.selectFirst("div#cont2 table#tablcont")
                val appealElement = document?.selectFirst("div#cont4 table#tablcont")

                val processSteps = processElement?.select("tr")
                    ?.drop(2)
                    ?.map { row ->
                        val tdElements = row.select("td")
                        ProcessStep(
                            event = tdElements[0].ownText(),
                            date = tdElements[1].ownText(),
                            time = tdElements[2].ownText(),
                            result = tdElements[4].ownText(),
                            cause = tdElements[5].ownText(),
                            dateOfPublishing = tdElements[7].ownText()
                        )
                    }?.toMutableList() ?: mutableListOf()

                logd("fetched process last: ${processSteps.last()}")
                logd("saved process last: ${savedCase.process.last()}")
                if (processSteps.last() != savedCase.process.last()) {
                    logd("Process has changed, sending notification...")
                    sendNotification(processSteps, savedCase.number, CaseEvent.Process)
                }

                val appealRows = appealElement?.select("tr")?.drop(2)
                appealRows?.forEach { row ->
                    val tdElements = row.select("td")
                    val fieldName = tdElements[0].text().trim()
                    val fieldValue = tdElements.last()?.text()?.trim() ?: ""
                    appeal[fieldName] = fieldValue
                }

                logd("fetched appeal: $appeal")
                logd("saved appeal: ${savedCase.appeal}")
                if (appeal != savedCase.appeal) {
                    logd("Appeal has changed, sending notification...")
                    sendNotification(emptyList(), savedCase.number, CaseEvent.Appeal)
                }
            }
            logd("No updates")
            Result.success()
        } catch (e: Exception) {
            logd("Work exception: ${e.localizedMessage}")
            Result.failure()
        }
    }

    private fun sendNotification(processSteps: List<ProcessStep>, number: String, event: CaseEvent) {
        when (event) {
            CaseEvent.Process -> {
                NotificationHelper(context).createNotification(
                    title = "Обновление по делу № $number",
                    message = "${processSteps.last().event} ${processSteps.last().date}",
                    notificationId = number.hashCode(),
                )
            }
            CaseEvent.Appeal -> {
                NotificationHelper(context).createNotification(
                    title = "Обновление по делу № $number",
                    message = "Подана жалоба!",
                    notificationId = number.hashCode(),
                )
            }
        }
    }
}

sealed class CaseEvent {
    object Process : CaseEvent()
    object Appeal : CaseEvent()
}

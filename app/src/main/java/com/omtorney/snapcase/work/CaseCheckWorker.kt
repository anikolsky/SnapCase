package com.omtorney.snapcase.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.omtorney.snapcase.common.domain.Repository
import com.omtorney.snapcase.common.domain.court.Courts
import com.omtorney.snapcase.common.domain.model.Case
import com.omtorney.snapcase.common.domain.model.ProcessStep
import com.omtorney.snapcase.common.domain.parser.PageParserFactory
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
                val court = Courts.getCourt(savedCase.courtTitle)
                val parser = PageParserFactory(repository).create(court)
                val document = repository.getJsoupDocument(savedCase.url)
                val processElement = document?.selectFirst("div#cont2 table#tablcont")
                val appealElement = document?.selectFirst("div#cont4 table#tablcont")

                val processSteps = parser.fetchProcess(processElement)
                logd("Process last\nfetched:\n${processSteps.last()}\nsaved:\n${savedCase.process.last()}")
                if (processSteps.last() != savedCase.process.last()) {
                    logd("Process has changed, sending notification...")
                    sendNotification(processSteps, savedCase, CaseEvent.Process)
                }

                val appeal = parser.fetchAppeal(appealElement)
                logd("Appeal\nfetched:\n$appeal\nsaved:\n${savedCase.appeal}")
                if (appeal != savedCase.appeal) {
                    logd("Appeal has changed, sending notification...")
                    sendNotification(emptyList(), savedCase, CaseEvent.Appeal)
                }
            }
            logd("No updates")
            Result.success()
        } catch (e: Exception) {
            logd("Work exception: ${e.localizedMessage}")
            Result.failure()
        }
    }

    private fun sendNotification(
        processSteps: List<ProcessStep>,
        case: Case,
        event: CaseEvent
    ) {
        val notificationHelper = NotificationHelper(context)
        val title = "Обновление по делу № ${case.number}"

        notificationHelper.createNotification(
            title = title,
            eventMessage = when (event) {
                CaseEvent.Process -> "${processSteps.last().event} ${processSteps.last().date}"
                CaseEvent.Appeal -> "Подана жалоба"
            },
            case = case,
            notificationId = case.number.hashCode()
        )
    }
}

sealed class CaseEvent {
    object Process : CaseEvent()
    object Appeal : CaseEvent()
}

package com.omtorney.snapcase.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.omtorney.snapcase.common.domain.Repository
import com.omtorney.snapcase.common.domain.model.Case
import com.omtorney.snapcase.common.domain.model.ProcessStep
import com.omtorney.snapcase.common.domain.usecase.CaseUseCases
import com.omtorney.snapcase.common.presentation.logd
import com.omtorney.snapcase.common.util.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class CaseCheckWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val caseUseCases: CaseUseCases,
    private val repository: Repository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        logd("Starting work...")
        return try {
            val favorites = caseUseCases.getFavoriteCases().first()
            favorites.forEach { case ->
                logd("Iterating through favorites...")
                val processList = mutableListOf<ProcessStep>()
                val appeal = mutableMapOf<String, String>()
                val page = repository.getJsoupDocument(case.url)
                val mainCard = page?.select("div[id=content]")
                val tabsContent = mainCard?.select("table[id=tablcont]")
                val tabTitles = tabsContent?.select("th")
                for (tab in tabsContent!!.indices) {
                    val tableLines = tabsContent[tab].select("tr")
                    repeat(2) { tableLines.removeFirstOrNull() }
                    for (line in tableLines.indices) {

                        if (tabTitles!![tab].text() == "ДВИЖЕНИЕ ДЕЛА") {
                            val processLineElements = tableLines[line].select("td")
                            for (elem in processLineElements.indices) {
                                if (processLineElements[elem].text().isEmpty())
                                    processLineElements[elem].text("")
                            }
                            val processElement = ProcessStep(
                                event = processLineElements[0].text(),
                                date = processLineElements[1].text(),
                                time = processLineElements[2].text(),
                                result = processLineElements[4].text(),
                                cause = processLineElements[5].text(),
                                dateOfPublishing = processLineElements[7].text()
                            )
                            processList.add(processElement)
                            logd("Process added: $processElement")
                        }
                        if (processList.last() != case.process.last()) {
                            logd("Process has changed, sending notification...")
                            sendNotification(case, CaseEvent.Process)
                        }

                        if (tabTitles[tab].text() == "ОБЖАЛОВАНИЕ РЕШЕНИЙ, ОПРЕДЕЛЕНИЙ (ПОСТ.)") {
                            val appealsLineElements = tableLines[line].select("td")
                            for (elem in appealsLineElements.indices) {
                                if (appealsLineElements[elem].text().isEmpty())
                                    appealsLineElements[elem].text("")
                            }
                            // Получаем только последнее обжалование
                            appeal[appealsLineElements[0].text()] = appealsLineElements[appealsLineElements.size - 1].text()
                        }

                        if (appeal != case.appeal) {
                            logd("Appeal has changed, sending notification...")
                            sendNotification(case, CaseEvent.Appeal)
                        }
                    }
                }
            }
            logd("Result.success without result")
            sendNotification(Case(), CaseEvent.NoEvent) // TODO remove test
            Result.success()
        } catch (e: Exception) {
            logd("Work exception: ${e.localizedMessage}")
            Result.failure()
        }
    }

    private fun sendNotification(case: Case, event: CaseEvent) {
        when (event) {
            CaseEvent.Process -> {
                NotificationHelper(context).createNotification(
                    title = "Обновление информации по делу",
                    message = "Движение дела № ${case.number}\n${case.participants}"
                )
            }
            CaseEvent.Appeal -> {
                NotificationHelper(context).createNotification(
                    title = "Обновление информации по делу",
                    message = "Обжалование по делу № ${case.number}\n${case.participants}"
                )
            }
            CaseEvent.NoEvent -> {
                NotificationHelper(context).createNotification(
                    title = "Проведена проверка",
                    message = "Обновления отсутствуют"
                )
            }
        }
    }
}

sealed class CaseEvent {
    object Process : CaseEvent()
    object Appeal : CaseEvent()
    object NoEvent : CaseEvent()
}

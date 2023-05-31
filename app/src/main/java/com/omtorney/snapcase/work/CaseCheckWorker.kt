package com.omtorney.snapcase.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.omtorney.snapcase.common.domain.Repository
import com.omtorney.snapcase.common.domain.model.Case
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
//    private val caseUseCases: CaseUseCases,
//    private val repository: Repository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        logd("starting...")
        sendNotification(Case())
        return Result.success()
//        logd("starting...")
//        return try {
//            val favorites = caseUseCases.getFavoriteCases().first()
//            favorites.forEach { case ->
//
//                logd("iterating through favorites...")
//
//                var eventInfo: String
//                val lastEvent = case.lastEvent
//                val process = mutableListOf<String>()
//                val appeal = mutableMapOf<String, String>()
//
//                val page = repository.getJsoupDocument(case.url)
//                val mainCard = page?.select("div[id=content]")
//                val tabsContent = mainCard?.select("table[id=tablcont]")
//                val tabTitles = tabsContent?.select("th")
//
//                for (tab in tabsContent!!.indices) {
//                    val tableLines = tabsContent[tab].select("tr")
//                    repeat(2) { tableLines.removeFirstOrNull() }
//
//                    for (line in tableLines.indices) {
//
//                        if (tabTitles!![tab].text() == "ДВИЖЕНИЕ ДЕЛА") {
//                            val processLineElements = tableLines[line].select("td")
//
//                            for (elem in processLineElements.indices) {
//                                if (processLineElements[elem].text().isEmpty())
//                                    processLineElements[elem].text("")
//                            }
//
//                            val date = processLineElements[1].text()
//                            val time = processLineElements[2].text()
//                            eventInfo = date + time
//
//                            logd("eventInfo: $eventInfo")
//
//                            process.add(eventInfo)
//
//                            if (process.last() != lastEvent) {
//                                logd("Sending notification...")
//                                sendNotification(case)
//                            }
//                        }
//
//                        if (tabTitles[tab].text() == "ОБЖАЛОВАНИЕ РЕШЕНИЙ, ОПРЕДЕЛЕНИЙ (ПОСТ.)") {
//                            val appealsLineElements = tableLines[line].select("td")
//                            for (elem in appealsLineElements.indices) {
//                                if (appealsLineElements[elem].text().isEmpty())
//                                    appealsLineElements[elem].text("")
//                            }
//                            // Получаем только актуальное обжалование
//                            appeal[appealsLineElements[0].text()] = appealsLineElements[appealsLineElements.size - 1].text()
//
//                            // Если обжалование было пустое, а стало не пустое - уведомление
//                            // Если обжалование было не пустое, но не соответствует новому - уведомление
//                            // Сравнение тоже по каким-то полям
//
//                        }
//                    }
//                }
//            }
//            logd("Result.success without result")
//            Result.success()
//        } catch (e: Exception) {
//            logd("exception: ${e.localizedMessage}")
//            Result.failure()
//        }
    }

    private fun sendNotification(case: Case) {
        NotificationHelper(context).createNotification(
            title = "Обновление информации по делу",
            message = "Новая запись по делу № ${case.number}\n${case.participants}"
        )
    }
}

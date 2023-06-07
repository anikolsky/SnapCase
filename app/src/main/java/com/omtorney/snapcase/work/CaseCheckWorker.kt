package com.omtorney.snapcase.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.omtorney.snapcase.common.domain.court.Courts
import com.omtorney.snapcase.common.domain.model.Case
import com.omtorney.snapcase.common.domain.usecase.CaseUseCases
import com.omtorney.snapcase.common.presentation.logd
import com.omtorney.snapcase.common.util.NotificationHelper
import com.omtorney.snapcase.common.util.Resource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class CaseCheckWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val caseUseCases: CaseUseCases,
//    private val repository: Repository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        logd("Starting work...")
        return try {
            val favorites = caseUseCases.getFavoriteCases().first()

            logd("Iterating through favorites...")
            favorites.forEach { savedCase ->
                val court = Courts.getCourt(savedCase.courtTitle)
                caseUseCases.fetchCase(savedCase, court).collect { result ->
                    when (result) {
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            logd("fetched process last: ${result.data?.process?.last()}")
                            logd("saved process last: ${savedCase.process.last()}")
                            if (result.data?.process?.last() != savedCase.process.last()) {
                                logd("Process has changed, sending notification...")
                                sendNotification(savedCase, CaseEvent.Process)
                            }

                            logd("fetched appeal: ${result.data?.appeal}")
                            logd("saved appeal: ${savedCase.appeal}")
                            if (result.data?.appeal != savedCase.appeal) {
                                logd("Appeal has changed, sending notification...")
                                sendNotification(savedCase, CaseEvent.Appeal)
                            }
                        }
                        is Resource.Error -> {}
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

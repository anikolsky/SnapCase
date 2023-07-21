package com.omtorney.snapcase.presentation.screen.recent

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.omtorney.snapcase.domain.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject

@HiltViewModel
class RecentViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {

    private val _state = mutableStateOf(RecentState())
    val state: State<RecentState> = _state

    private var getRecentCasesJob: Job? = null

    init {
//        getRecentCases()
    }

//    private fun getRecentCases() {
//        getRecentCasesJob?.cancel()
//        getRecentCasesJob = useCases.getRecentCases().onEach { cases ->
//            _state.value = state.value.copy(cases = cases)
//        }.launchIn(viewModelScope)
//    }

//    fun onEvent(event: RecentEvent) {
//        when (event) {
//            is RecentEvent.Clear -> {
//                viewModelScope.launch {
//                    useCases.clearRecentCases()
//                }
//            }
//            is RecentEvent.Refresh -> {
//
//            }
//        }
//    }
}
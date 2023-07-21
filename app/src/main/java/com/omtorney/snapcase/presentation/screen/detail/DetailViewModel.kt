package com.omtorney.snapcase.presentation.screen.detail

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.domain.court.Courts
import com.omtorney.snapcase.domain.model.Case
import com.omtorney.snapcase.domain.usecase.UseCases
import com.omtorney.snapcase.presentation.components.UiEvent
import com.omtorney.snapcase.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val useCases: UseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(DetailState())
    val state: State<DetailState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        fun dec(arg: String) = Uri.decode(savedStateHandle.get<String>(arg)) ?: ""

        val case = Case(
            number = dec("number"),
            uid = dec("uid"),
            url = dec("url"),
            permanentUrl = dec("permanentUrl"),
            courtTitle = dec("courtTitle"),
            type = "",
            category = "",
            judge = "",
            participants = "",
            receiptDate = "",
            hearingDateTime = dec("hearingDateTime"),
            result = "",
            actDateTime = "",
            actDateForce = dec("actDateForce"),
            actTextUrl = dec("actTextUrl"),
            notes = ""
        )

        if (case.uid.isNotEmpty()) {
            _state.value = DetailState(case = case, isFavorite = true)
        } else {
            _state.value = DetailState(case = case)
        }

        onEvent(DetailEvent.Load(Courts.getCourt(case.courtTitle)))
    }

    fun onEvent(event: DetailEvent) {
        when (event) {
            is DetailEvent.Save -> {
                viewModelScope.launch {
                    useCases.saveCase(event.case)
                    checkIsFavorite()
                    _eventFlow.emit(UiEvent.Save)
                }
            }

            is DetailEvent.Delete -> {
                viewModelScope.launch {
                    useCases.deleteCase(event.case)
                    checkIsFavorite()
                    _state.value = state.value.copy(isFavorite = false)
                    _eventFlow.emit(UiEvent.Delete)
                }
            }

            is DetailEvent.Load -> {
                viewModelScope.launch {
                    useCases.fetchCase(state.value.case, event.court, state.value.isFavorite).collect { result ->
                        when (result) {
                            is Resource.Loading -> {
                                _state.value = state.value.copy(isLoading = true)
                            }

                            is Resource.Success -> {
                                _state.value = state.value.copy(
                                    case = result.data ?: Case("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""),
                                    isLoading = false
                                )
                                if (state.value.isFavorite) {
                                    useCases.saveCase(state.value.case)
                                }
                            }

                            is Resource.Error -> {
                                _state.value = state.value.copy(
                                    error = result.message ?: "Unexpected error",
                                    isLoading = false
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun checkIsFavorite() {
        viewModelScope.launch {
            val isFavorite = useCases.checkCase(state.value.case.uid)
            _state.value = state.value.copy(isFavorite = isFavorite)
        }
    }
}

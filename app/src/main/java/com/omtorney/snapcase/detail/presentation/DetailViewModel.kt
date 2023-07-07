package com.omtorney.snapcase.detail.presentation

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.common.domain.court.Courts
import com.omtorney.snapcase.common.domain.model.Case
import com.omtorney.snapcase.common.domain.usecase.UseCases
import com.omtorney.snapcase.common.presentation.components.UiEvent
import com.omtorney.snapcase.common.util.Resource
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
        val url = Uri.decode(savedStateHandle.get<String>("url")) ?: ""
        val number = Uri.decode(savedStateHandle.get<String>("number")) ?: ""
        val hearingDateTime = savedStateHandle.get<String>("hearingDateTime") ?: ""
        val actDateForce = savedStateHandle.get<String>("actDateForce") ?: ""
        val actTextUrl = Uri.decode(savedStateHandle.get<String>("actTextUrl")) ?: ""
        val courtTitle = savedStateHandle.get<String>("courtTitle") ?: ""
        _state.value = DetailState(
            case = Case(
                number = number,
                uid = "",
                url = url,
                courtTitle = courtTitle,
                type = "",
                category = "",
                judge = "",
                participants = "",
                receiptDate = "",
                hearingDateTime = hearingDateTime,
                result = "",
                actDateTime = "",
                actDateForce = actDateForce,
                actTextUrl = actTextUrl,
                notes = ""
            )
        )
        onEvent(DetailEvent.Load(Courts.getCourt(courtTitle)))
        checkFavorite()
    }

    fun onEvent(event: DetailEvent) {
        when (event) {
            is DetailEvent.Save -> {
                viewModelScope.launch {
                    useCases.saveCase(event.case)
                    checkFavorite()
                    _eventFlow.emit(UiEvent.Save)
                }
            }

            is DetailEvent.Delete -> {
                viewModelScope.launch {
                    useCases.deleteCase(event.case)
                    checkFavorite()
                    _eventFlow.emit(UiEvent.Delete)
                }
            }

            is DetailEvent.Load -> {
                viewModelScope.launch {
                    useCases.fetchCase(state.value.case, event.court).collect { result ->
                        when (result) {
                            is Resource.Loading -> {
                                _state.value = state.value.copy(isLoading = true)
                            }

                            is Resource.Success -> {
                                _state.value = state.value.copy(
                                    case = result.data ?: Case("", "", "", "", "", "", "", "", "", "", "", "", "", "", ""),
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

    private fun checkFavorite() {
        viewModelScope.launch {
            val isFavorite = useCases.checkCase(state.value.case.number)
            _state.value = state.value.copy(isFavorite = isFavorite)
        }
    }
}

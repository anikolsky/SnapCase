package com.omtorney.snapcase.act.presentation

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.common.domain.court.Courts
import com.omtorney.snapcase.common.domain.usecase.UseCases
import com.omtorney.snapcase.common.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActViewModel @Inject constructor(
    private val useCases: UseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(ActState())
    val state: State<ActState> = _state

    init {
        savedStateHandle.get<String>("url")?.let { url ->
            loadAct(Uri.decode(url))
        }
    }

    private fun loadAct(url: String) = viewModelScope.launch {
        useCases.loadActText(Courts.Dmitrov, url).collect { result ->
            when (result) {
                is Resource.Loading -> {
                    _state.value = ActState(isLoading = true)
                }

                is Resource.Success -> {
                    _state.value = ActState(text = result.data ?: "")
                }

                is Resource.Error -> {
                    _state.value = ActState(error = result.message ?: "Unexpected error")
                }
            }
        }
    }
}

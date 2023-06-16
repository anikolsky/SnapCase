package com.omtorney.snapcase.act.presentation

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
        val courtTitle = savedStateHandle.get<String>("courtTitle")!!
        val url = savedStateHandle.get<String>("url")!!
        loadAct(courtTitle, Uri.decode(url))
    }

    private fun loadAct(courtTitle: String, url: String) = viewModelScope.launch {
        useCases.loadActText(courtTitle, url).collect { result ->
            when (result) {
                is Resource.Loading -> {
                    _state.value = ActState(isLoading = true)
                }

                is Resource.Success -> {
                    _state.value = ActState(text = result.data ?: "", url = url)
                }

                is Resource.Error -> {
                    _state.value = ActState(error = result.message ?: "Unexpected error")
                }
            }
        }
    }
}

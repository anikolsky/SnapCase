package com.omtorney.snapcase.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.util.Constants
import com.omtorney.snapcase.firebase.auth.SignInResult
import com.omtorney.snapcase.firebase.auth.SignInState
import com.omtorney.snapcase.domain.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    useCases: UseCases
): ViewModel() {

    val accentColor: StateFlow<Long> = useCases.getAccentColor().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = Constants.INITIAL_COLOR
    )

    val isDarkThemeEnabled: StateFlow<Boolean> = useCases.isDarkThemeEnabled().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = false
    )

    /** Sign in */

    private val _signInState = MutableStateFlow(SignInState())
    val signInState = _signInState.asStateFlow()

    fun onSignInResult(result: SignInResult) {
        _signInState.update { it.copy(
            isSignInSuccessful = result.data != null,
            signInErrorMessage = result.errorMessage
        ) }
//        _signInState.value = signInState.value.copy(
//            isSignInSuccessful = result.data != null,
//            signInErrorMessage = result.errorMessage
//        )
    }

    fun resetState() {
        _signInState.update { SignInState() }
//        _signInState.value = SignInState()
    }
}

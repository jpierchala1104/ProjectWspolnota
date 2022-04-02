package com.example.projektwspolnota.login.mvi

import com.example.projektwspolnota.dataClasses.Operation
import com.example.projektwspolnota.services.SessionService

class LoginStateReducer{
    fun reduce(previousState: LoginState, stateChange: LoginStateChanges): LoginState {
        val currentState = previousState.copy()
        currentState.errorMessage = null

        when(stateChange){
            is LoginStateChanges.LoggedIn -> {
                when (stateChange.operation){
                    is Operation.InProgress -> {
                        currentState.isLoggingIn = true
                        currentState.loggedIn = false
                    }
                    is Operation.Completed -> {
                        when {
                            SessionService.getCurrentUser()?.userId == 0 ->
                                currentState.errorMessage = "Zły login lub hasło"
                            SessionService.getCurrentUser() == null ->
                                currentState.errorMessage = "Nie udało się połączyć do servera"
                        }
                        currentState.isLoggingIn = false
                    }
                    is Operation.Failed -> {
                        currentState.errorMessage = "Nie udało się połączyć do servera"
                        currentState.isLoggingIn = false
                    }
                }
            }
        }
        return currentState
    }
}
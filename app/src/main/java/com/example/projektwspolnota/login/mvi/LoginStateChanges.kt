package com.example.projektwspolnota.login.mvi

import com.example.projektwspolnota.dataClasses.Operation
import com.example.projektwspolnota.services.User

sealed class LoginStateChanges {
    data class LoggedIn(val operation: Operation<User>): LoginStateChanges()
}
package com.example.projektwspolnota.login.mvi

data class LoginState (
    var isLoggingIn: Boolean,
    var loggedIn: Boolean,
    var errorMessage: String?
)
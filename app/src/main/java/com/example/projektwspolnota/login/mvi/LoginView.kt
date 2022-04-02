package com.example.projektwspolnota.login.mvi

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

interface LoginView : MvpView {
    fun render(state: LoginState)

    val initIntent: Observable<Unit>
    val loginIntent: Observable<Credentials>
}
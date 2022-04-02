package com.example.projektwspolnota.login.mvi

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers

class LoginViewPresenter(private val loginComponentUseCase: LoginComponentUseCase) :
    MviBasePresenter<LoginView, LoginState>() {

    private val reducer = LoginStateReducer()

    override fun bindIntents() {
//        val initIntent = intent { it.initIntent }
//            .switchMap { loginComponentUseCase. }

        val loginIntent = intent { it.loginIntent }
            .switchMap { loginComponentUseCase.logIn(it.login, it.password) }

        val initialState = LoginState(
            false,
            false,
            null
        )

        val reducedStream = loginIntent.scan(initialState) {
                previousState, stateChange -> reducer.reduce(previousState, stateChange)
        }.observeOn(AndroidSchedulers.mainThread())

        subscribeViewState(reducedStream, LoginView::render)
    }
}

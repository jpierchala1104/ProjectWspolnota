package com.example.projektwspolnota.login.mvi

import com.example.projektwspolnota.dataClasses.Operation
import com.example.projektwspolnota.services.SessionService
import com.example.projektwspolnota.services.User
import io.reactivex.Observable

class LoginComponentUseCase(private val sessionService: SessionService) {

    fun logIn(login: String, password: String): Observable<LoginStateChanges> {
        return sessionService.login(login, password)
            .map { Operation.Completed<User>(it) as Operation<User>}
            .startWith(Operation.InProgress<User>())
            .onErrorReturn { Operation.Failed<User>(it) }
            .map{ LoginStateChanges.LoggedIn(it) }
    }
}
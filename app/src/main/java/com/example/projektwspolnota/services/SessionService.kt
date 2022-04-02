package com.example.projektwspolnota.services

import io.reactivex.Observable

object SessionService {
    private var currentUser: User? = null

    fun login(login: String, password: String): Observable<User?> {
        return ApiRequestService().logIn(login, password)
    }

    fun isAuthenticated(): Boolean {
        return (currentUser != null && currentUser?.userId != 0)
    }

    fun getCurrentUser(): User? {
        return currentUser
    }

    fun setCurrentUser(user:User?){
        currentUser = user
    }
}
package com.example.projektwspolnota.resolution.mvi

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

interface ResolutionView : MvpView {
    fun render(state: ResolutionState)

    val initIntent: Observable<Unit>
    val loadNextIntent: Observable<LoadNextPageParam>
    val logOutIntent: Observable<Unit>
    val sendMessageIntent: Observable<Unit>
    val confirmIntent: Observable<String>
    val denyIntent: Observable<Unit>
}
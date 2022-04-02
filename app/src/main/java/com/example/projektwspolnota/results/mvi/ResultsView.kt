package com.example.projektwspolnota.results.mvi

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

interface ResultsView: MvpView {
    fun render(state: ResultsState)

    val initIntent: Observable<Int>
    val exitIntent: Observable<Unit>
}
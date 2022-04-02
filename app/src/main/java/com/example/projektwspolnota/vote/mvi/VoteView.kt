package com.example.projektwspolnota.vote.mvi

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

interface VoteView: MvpView {
    fun render(state: VoteState)

    val initIntent: Observable<Int>
    val sendIntent: Observable<Unit>
    val confirmIntent: Observable<Unit>
    val denyIntent: Observable<Unit>
    val exitIntent: Observable<Unit>
    val voteIntent: Observable<Vote>
}
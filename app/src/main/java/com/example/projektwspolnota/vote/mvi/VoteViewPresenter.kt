package com.example.projektwspolnota.vote.mvi

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

class VoteViewPresenter(private val voteComponentUseCase: VoteComponentUseCase) :
    MviBasePresenter<VoteView, VoteState>() {

    private var lastState = VoteState(
        isLoading = true,
        isVoteListLoading = true,
        isVoteListVisible = false,
        isConfirmLayoutVisible = false,
        isClosingActivity = false,
        errorMessage = null,
        voteItem = VoteItem(0, "", "0.0.0", "0.0.0", listOf()))

    private val reducer = VoteStateReducer()

    override fun bindIntents() {
        val initVoteIntent = intent { it.initIntent }
            .switchMap { voteComponentUseCase.initPointList(it) }

        val sendVoteIntent = intent { it.sendIntent }
            .switchMap { voteComponentUseCase.sendResolution() }

        val confirmVoteIntent = intent { it.confirmIntent }
            .switchMap { voteComponentUseCase.confirmSend(lastState.voteItem) }

        val denyVoteIntent = intent { it.denyIntent }
            .switchMap { voteComponentUseCase.denySending() }

        val exitVotingIntent = intent { it.exitIntent }
            .switchMap { voteComponentUseCase.exitVoting() }

        val voteIntent = intent { it.voteIntent }
            .switchMap { voteComponentUseCase.vote(it) }

        val observables = listOf(
            initVoteIntent,
            sendVoteIntent,
            confirmVoteIntent,
            denyVoteIntent,
            exitVotingIntent,
            voteIntent
        )

        val stream = Observable.merge(observables)

        val reducedStream = stream.scan(lastState) { previousState, stateChange ->
            val reducedState = reducer.reduce(previousState, stateChange)
            lastState = reducedState
            return@scan reducedState
        }.observeOn(AndroidSchedulers.mainThread())

        subscribeViewState(reducedStream, VoteView::render)
    }
}
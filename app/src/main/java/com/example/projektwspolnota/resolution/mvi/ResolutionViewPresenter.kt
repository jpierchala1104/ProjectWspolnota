package com.example.projektwspolnota.resolution.mvi

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

class ResolutionViewPresenter(private val resolutionComponentUseCase: ResolutionComponentUseCase):
    MviBasePresenter<ResolutionView, ResolutionState>() {

    private val reducer = ResolutionStateReducer()

    override fun bindIntents() {
        val initMessageIntent = intent { it.initIntent }
            .switchMap { resolutionComponentUseCase.initMessage()}

        val initResolutionIntent = intent { it.initIntent }
            .switchMap { resolutionComponentUseCase.initResolutionList()}

        val initResultIntent = intent { it.initIntent }
            .switchMap { resolutionComponentUseCase.initResultList()}

        val loadNextIntent = intent { it.loadNextIntent}
            .concatMap { resolutionComponentUseCase.loadNextPage( it.skip, it.take) }

        val sendMessageIntent = intent { it.sendMessageIntent }
            .switchMap { resolutionComponentUseCase.sendMessage() }

        val confirmIntent = intent { it.confirmIntent }
            .switchMap { resolutionComponentUseCase.confirmSend(it) }

        val denyIntent = intent { it.denyIntent }
            .switchMap { resolutionComponentUseCase.denySending() }

        val logOutIntent = intent { it.logOutIntent }
            .switchMap { resolutionComponentUseCase.logOut() }

        val observables = listOf(
            initMessageIntent,
            initResolutionIntent,
            initResultIntent,
            loadNextIntent,
            sendMessageIntent,
            confirmIntent,
            denyIntent,
            logOutIntent
        )

        val stream = Observable.merge(observables)

        val initialState = ResolutionState(
            isLoading = true,
            isMessageVisible = false,
            message = Message("@string/WelcomeText", ""),
            isConfirmLayoutVisible = false,
            isResolutionListVisible = false,
            isResultListVisible = false,
            isLogingOut = false,
            isRefreshing = false,
            isLoadingNextPage = false,
            errorMessage = null,
            resolutionCollection = listOf(),
            resultCollection = listOf()
        )
        val reducedStream = stream.scan(initialState){
                previousState, stateChange -> reducer.reduce(previousState,stateChange)
        }.observeOn(AndroidSchedulers.mainThread())

        subscribeViewState(reducedStream, ResolutionView::render)
    }
}
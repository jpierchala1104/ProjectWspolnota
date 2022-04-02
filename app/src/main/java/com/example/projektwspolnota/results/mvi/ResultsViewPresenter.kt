package com.example.projektwspolnota.results.mvi

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

class ResultsViewPresenter(private val resultsComponentUseCase: ResultsComponentUseCase) :
    MviBasePresenter<ResultsView, ResultsState>() {

    private val reducer = ResultsStateReducer()

    override fun bindIntents() {
        val initResultsIntent = intent { it.initIntent }
            .switchMap { resultsComponentUseCase.initPointList(it) }

        val exitVotingIntent = intent { it.exitIntent }
            .switchMap { resultsComponentUseCase.exitVoting() }

        val stream = Observable.merge(initResultsIntent, exitVotingIntent)

        val initialState = ResultsState(
            isLoading = true,
            isResultsListLoading = true,
            isResultsListVisible = false,
            isClosingActivity = false,
            errorMessage = null,
            resultsItem = ResultsItem(0, "", "0.0.0", listOf())
        )

        val reducedStream = stream.scan(initialState) { previousState, stateChange ->
            reducer.reduce(previousState, stateChange)
        }.observeOn(AndroidSchedulers.mainThread())

        subscribeViewState(reducedStream, ResultsView::render)
    }
}
package com.example.projektwspolnota.results.mvi

import com.example.projektwspolnota.services.ApiRequestService
import com.example.projektwspolnota.dataClasses.Operation
import io.reactivex.Observable

class ResultsComponentUseCase(private val apiRequestService: ApiRequestService) {

    fun initPointList(resolutionId: Int): Observable<ResultsStateChanges> {
        return loadPoints(resolutionId)
            .map { ResultsStateChanges.InitializingResultsList(it) }
    }

    fun exitVoting(): Observable<ResultsStateChanges> {
        return Observable.just(ResultsStateChanges.ExitingResults)
    }

    private fun loadPoints(
        resolutionId: Int
    ): Observable<Operation<ResultsItem>> {

        return apiRequestService.loadPointsResults(resolutionId)
            .map { Operation.Completed<ResultsItem>(it) as Operation<ResultsItem> }
            .startWith(Operation.InProgress<ResultsItem>())
            .onErrorReturn {
                Operation.Failed<ResultsItem>(
                    it
                )
            }
    }
}
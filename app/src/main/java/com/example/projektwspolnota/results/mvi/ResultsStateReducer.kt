package com.example.projektwspolnota.results.mvi

import com.example.projektwspolnota.dataClasses.Operation

class ResultsStateReducer {
    fun reduce(previousState: ResultsState, stateChange: ResultsStateChanges): ResultsState {
        val currentState = previousState.copy()
        currentState.errorMessage = null

        when (stateChange) {
            is ResultsStateChanges.InitializingResultsList -> {
                when (stateChange.operation) {
                    is Operation.InProgress -> {
                        currentState.isLoading = true
                        currentState.isResultsListLoading = true
                        currentState.isResultsListVisible = false
                    }
                    is Operation.Completed -> {
                        currentState.isResultsListVisible = true
                        currentState.isResultsListLoading = false
                        currentState.isLoading = false
                        currentState.resultsItem = stateChange.operation.value
                    }
                    is Operation.Failed -> {
                        currentState.errorMessage = "Initializing resolution list failed"
                        currentState.isResultsListVisible = true
                        currentState.isResultsListLoading = false
                        currentState.isLoading = false
                    }
                }
            }
            is ResultsStateChanges.ExitingResults -> {
                currentState.isClosingActivity = true
            }
        }
        return currentState
    }
}
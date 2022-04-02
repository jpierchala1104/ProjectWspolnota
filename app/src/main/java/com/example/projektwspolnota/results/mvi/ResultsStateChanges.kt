package com.example.projektwspolnota.results.mvi

import com.example.projektwspolnota.dataClasses.Operation

sealed class ResultsStateChanges {
    data class InitializingResultsList(val operation: Operation<ResultsItem>) : ResultsStateChanges()
    object ExitingResults : ResultsStateChanges()
}
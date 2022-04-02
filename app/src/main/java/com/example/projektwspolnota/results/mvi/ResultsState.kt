package com.example.projektwspolnota.results.mvi

data class ResultsState (
    var isLoading: Boolean,
    var isResultsListLoading: Boolean,
    var isResultsListVisible: Boolean,
    var isClosingActivity: Boolean,
    var errorMessage: String?,
    var resultsItem: ResultsItem
)
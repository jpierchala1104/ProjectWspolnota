package com.example.projektwspolnota.results.mvi

data class ResultsPointItem(
    val number: Int,
    val text: String,
    val yesVotePercentage: Int,
    val noVotePercentage: Int,
    val abstainPercentage: Int,
    val numberOfVotes: Int
)
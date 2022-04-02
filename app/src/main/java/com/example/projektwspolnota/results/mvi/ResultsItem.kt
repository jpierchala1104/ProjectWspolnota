package com.example.projektwspolnota.results.mvi

data class ResultsItem(val resolutionId: Int, val title: String, val date: String, val pointsResults: Collection<ResultsPointItem>) {
    override fun toString(): String = date
}
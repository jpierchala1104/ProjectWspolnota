package com.example.projektwspolnota.vote.mvi

data class VoteItem(val resolutionId: Int, val title: String, val date: String, val finishDate: String, val points: Collection<VotePointItem>) {
    override fun toString(): String = date
}
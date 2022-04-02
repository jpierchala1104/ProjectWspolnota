package com.example.projektwspolnota.resolution.mvi

data class ResolutionItem(val resolutionId: Int, val title: String, val date: String, val finishDate: String) {
    override fun toString(): String = date
}
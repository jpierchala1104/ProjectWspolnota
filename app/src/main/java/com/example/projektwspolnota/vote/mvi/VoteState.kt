package com.example.projektwspolnota.vote.mvi

data class VoteState (
    var isLoading: Boolean,
    var isVoteListLoading: Boolean,
    var isVoteListVisible: Boolean,
    var isConfirmLayoutVisible: Boolean,
    var isClosingActivity: Boolean,
    var errorMessage: String?,
    var voteItem: VoteItem
)
package com.example.projektwspolnota.vote.mvi

import com.example.projektwspolnota.dataClasses.Operation

sealed class VoteStateChanges {
    data class InitializingVoteList(val operation: Operation<VoteItem>) : VoteStateChanges()
    object SendingResolution : VoteStateChanges()
    data class ConfirmSending(val operation: Operation<Unit>) : VoteStateChanges()
    object DenySending : VoteStateChanges()
    object ExitingVoting : VoteStateChanges()
    data class Voted(val vote: Vote) : VoteStateChanges()
}
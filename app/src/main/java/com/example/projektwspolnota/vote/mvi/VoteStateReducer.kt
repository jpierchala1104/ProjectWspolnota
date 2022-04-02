package com.example.projektwspolnota.vote.mvi

import com.example.projektwspolnota.dataClasses.Operation

class VoteStateReducer {
    fun reduce(previousState: VoteState, stateChange: VoteStateChanges): VoteState {
        val currentState = previousState.copy()
        currentState.errorMessage = null

        when (stateChange) {
            is VoteStateChanges.InitializingVoteList -> {
                when (stateChange.operation) {
                    is Operation.InProgress -> {
                        currentState.isLoading = true
                        currentState.isVoteListLoading = true
                        currentState.isVoteListVisible = false
                    }
                    is Operation.Completed -> {
                        currentState.isVoteListVisible = true
                        currentState.isVoteListLoading = false
                        currentState.isLoading = false
                        currentState.voteItem = stateChange.operation.value
                    }
                    is Operation.Failed -> {
                        currentState.errorMessage = "Initializing resolution list failed"
                        currentState.isVoteListVisible = true
                        currentState.isVoteListLoading = false
                        currentState.isLoading = false
                    }
                }
            }
            is VoteStateChanges.SendingResolution -> {
                currentState.isConfirmLayoutVisible = true
            }
            is VoteStateChanges.ConfirmSending -> {
                when (stateChange.operation) {
                    is Operation.InProgress -> {
                        currentState.isLoading = true
                    }
                    is Operation.Completed -> {
                        currentState.isLoading = false
                        currentState.isClosingActivity = true
                    }
                    is Operation.Failed -> {
                        currentState.errorMessage = "Can't confirm send"
                        currentState.isLoading = false
                        currentState.isConfirmLayoutVisible = false
                    }
                }
            }
            is VoteStateChanges.DenySending -> {
                currentState.isConfirmLayoutVisible = false
            }
            is VoteStateChanges.ExitingVoting -> {
                currentState.isClosingActivity = true
            }
            is VoteStateChanges.Voted -> {
                val currentVote = currentState.voteItem.points.singleOrNull {
                    it.number == stateChange.vote.pointNumber
                }
                currentVote?.pointVote = stateChange.vote.vote
            }
        }
        return currentState
    }
}
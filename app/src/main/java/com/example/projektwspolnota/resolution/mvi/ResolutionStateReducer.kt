package com.example.projektwspolnota.resolution.mvi

import com.example.projektwspolnota.dataClasses.Operation

class ResolutionStateReducer {
    fun reduce(previousState: ResolutionState, stateChange: ResolutionStateChanges): ResolutionState {
        val currentState = previousState.copy()
        currentState.errorMessage = null

        when (stateChange) {
            is ResolutionStateChanges.InitializingResolutionList -> {
                when (stateChange.operation) {
                    is Operation.InProgress -> {
                        currentState.isRefreshing = false
                        currentState.isLoading = true
                        currentState.isResolutionListVisible = false
                    }
                    is Operation.Completed -> {
                        currentState.isLoading = false
                        currentState.isResolutionListVisible = true
                        currentState.resolutionCollection = stateChange.operation.value
                    }
                    is Operation.Failed -> {
                        currentState.errorMessage = "Initializing resolution list failed"
                        currentState.isLoading = false
                        currentState.isResolutionListVisible = true
                    }
                }
            }
            is ResolutionStateChanges.InitializingResultList -> {
                when (stateChange.operation) {
                    is Operation.InProgress -> {
                        currentState.isRefreshing = false
                        currentState.isLoading = true
                        currentState.isResultListVisible = false
                    }
                    is Operation.Completed -> {
                        currentState.isLoading = false
                        currentState.isResultListVisible = true
                        currentState.resultCollection = stateChange.operation.value
                    }
                    is Operation.Failed -> {
                        currentState.errorMessage = "Initializing result list failed"
                        currentState.isLoading = false
                        currentState.isResultListVisible = true
                    }
                }
            }
            is ResolutionStateChanges.InitializingMessage -> {
                when (stateChange.operation) {
                    is Operation.InProgress -> {
                        currentState.isRefreshing = false
                        currentState.isLoading = true
                        currentState.isMessageVisible = false
                    }
                    is Operation.Completed -> {
                        currentState.isLoading = false
                        currentState.isMessageVisible = true
                        currentState.message = stateChange.operation.value
                    }
                    is Operation.Failed -> {
                        currentState.errorMessage = "Initializing Message failed"
                        currentState.isLoading = false
                        currentState.isMessageVisible = true
                    }
                }
            }
            is ResolutionStateChanges.NextPageLoading -> {
                when (stateChange.operation) {
                    is Operation.InProgress -> {
                        currentState.isLoadingNextPage = true
                    }
                    is Operation.Completed -> {
                        currentState.isLoadingNextPage = false
                        currentState.resultCollection += stateChange.operation.value
                    }
                    is Operation.Failed -> {
                        currentState.errorMessage = "Loading failed"
                        currentState.isLoadingNextPage = false
                    }
                }
            }
            is ResolutionStateChanges.SendingMessage -> {
                currentState.isConfirmLayoutVisible = true
            }
            is ResolutionStateChanges.ConfirmSending -> {
                when (stateChange.operation) {
                    is Operation.InProgress -> {
                        currentState.isLoading = true
                    }
                    is Operation.Completed -> {
                        currentState.isLoading = false
                        currentState.isConfirmLayoutVisible = false
                        currentState.isRefreshing = true
                    }
                    is Operation.Failed -> {
                        currentState.errorMessage = "Can't confirm send"
                        currentState.isLoading = false
                        currentState.isConfirmLayoutVisible = false
                    }
                }
            }
            is ResolutionStateChanges.DenySending -> {
                currentState.isConfirmLayoutVisible = false
            }
            is ResolutionStateChanges.LogingOut -> {
                currentState.isLogingOut = true
            }
        }
        return currentState
    }
}
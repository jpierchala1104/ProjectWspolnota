package com.example.projektwspolnota.resolution.mvi

import com.example.projektwspolnota.dataClasses.Operation

sealed class ResolutionStateChanges {
    data class InitializingMessage(val operation: Operation<Message>) : ResolutionStateChanges()
    data class InitializingResolutionList(val operation: Operation<Collection<ResolutionItem>>): ResolutionStateChanges()
    data class InitializingResultList(val operation: Operation<Collection<ResolutionItem>>): ResolutionStateChanges()
    data class NextPageLoading(val operation: Operation<Collection<ResolutionItem>>) : ResolutionStateChanges()
    object DenySending : ResolutionStateChanges()
    object LogingOut : ResolutionStateChanges()
    object SendingMessage : ResolutionStateChanges()
    data class ConfirmSending(val operation: Operation<Unit>) : ResolutionStateChanges()
}
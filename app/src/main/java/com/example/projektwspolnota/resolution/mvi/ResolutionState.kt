package com.example.projektwspolnota.resolution.mvi

data class ResolutionState (
    var isLoading: Boolean,
    var isMessageVisible: Boolean,
    var message: Message,
    var isResolutionListVisible: Boolean,
    var isResultListVisible: Boolean,
    var isLoadingNextPage: Boolean,
    var isConfirmLayoutVisible: Boolean,
    var isRefreshing: Boolean,
    var isLogingOut: Boolean,
    var errorMessage: String?,
    var resolutionCollection: Collection<ResolutionItem>,
    var resultCollection: Collection<ResolutionItem>
)
package com.example.projektwspolnota.vote.mvi

import com.example.projektwspolnota.services.ApiRequestService
import com.example.projektwspolnota.dataClasses.Operation
import com.example.projektwspolnota.services.SessionService
import io.reactivex.Observable

class VoteComponentUseCase(private val apiRequestService: ApiRequestService, private val sessionService: SessionService) {

    fun initPointList(resolutionId: Int): Observable<VoteStateChanges> {
        return loadPoints(resolutionId)
            .map { VoteStateChanges.InitializingVoteList(it) }
    }

    fun sendResolution(): Observable<VoteStateChanges> {
        return Observable.just(VoteStateChanges.SendingResolution)
    }

    fun confirmSend(voteItem: VoteItem): Observable<VoteStateChanges> {
        return confirm(voteItem)
            .map { VoteStateChanges.ConfirmSending(it) }
    }

    fun denySending(): Observable<VoteStateChanges> {
        return Observable.just(VoteStateChanges.DenySending)
    }

    fun exitVoting(): Observable<VoteStateChanges> {
        return Observable.just(VoteStateChanges.ExitingVoting)
    }

    fun vote(vote: Vote): Observable<VoteStateChanges.Voted> {
        return Observable.just(VoteStateChanges.Voted(vote))
    }

    private fun loadPoints(
        resolutionId: Int
    ): Observable<Operation<VoteItem>> {

        return apiRequestService.loadPoints(resolutionId)
            .map { Operation.Completed<VoteItem>(it) as Operation<VoteItem> }
            .startWith(Operation.InProgress<VoteItem>())
            .onErrorReturn {
                Operation.Failed<VoteItem>(
                    it
                )
            }
    }

    private fun confirm(voteItem: VoteItem): Observable<Operation<Unit>> {
        if (sessionService.getCurrentUser() != null) {
            return apiRequestService.sendResolution(sessionService.getCurrentUser()!!, voteItem)
                .map { Operation.Completed<Unit>(it) as Operation<Unit> }
                .startWith(Operation.InProgress<Unit>())
                .onErrorReturn {
                    Operation.Failed<Unit>(it)
                }
        } else {
            throw Exception("User is null")
        }
    }
}

package com.example.projektwspolnota.resolution.mvi

import com.example.projektwspolnota.dataClasses.Operation
import com.example.projektwspolnota.services.ApiRequestService
import com.example.projektwspolnota.services.SessionService
import io.reactivex.Observable
import java.lang.Exception

class ResolutionComponentUseCase(private val apiRequestService: ApiRequestService, private val sessionService: SessionService) {

    fun initResolutionList(): Observable<ResolutionStateChanges> {
        return loadResolutions()
            .map { ResolutionStateChanges.InitializingResolutionList(it) }
    }

    fun initMessage(): Observable<ResolutionStateChanges> {
        return loadMessage()
            .map { ResolutionStateChanges.InitializingMessage(it) }
    }

    fun initResultList(): Observable<ResolutionStateChanges> {
        return loadResults(0, 5)
            .map { ResolutionStateChanges.InitializingResultList(it) }
    }

    fun loadNextPage(skip: Int, take: Int): Observable<ResolutionStateChanges> {
        return loadResults(skip+1, take)
            .map { ResolutionStateChanges.NextPageLoading(it) }
    }

    fun sendMessage(): Observable<ResolutionStateChanges> {
        return Observable.just(ResolutionStateChanges.SendingMessage)
    }

    fun confirmSend(text: String): Observable<ResolutionStateChanges> {
        return confirm(text)
            .map { ResolutionStateChanges.ConfirmSending(it) }
    }

    fun denySending(): Observable<ResolutionStateChanges> {
        return Observable.just(ResolutionStateChanges.DenySending)
    }

    fun logOut(): Observable<ResolutionStateChanges> {
        return Observable.just(ResolutionStateChanges.LogingOut)
    }

    private fun loadResolutions(): Observable<Operation<Collection<ResolutionItem>>> {
        if (sessionService.getCurrentUser() != null) {
            return apiRequestService.loadResolutions(sessionService.getCurrentUser()!!)
                .map { Operation.Completed<Collection<ResolutionItem>>(it) as Operation<Collection<ResolutionItem>> }
                .startWith(Operation.InProgress<Collection<ResolutionItem>>())
                .onErrorReturn {
                    Operation.Failed<Collection<ResolutionItem>>(
                        it
                    )
                }
        } else {
            throw Exception("User is null")
        }
    }

    private fun loadMessage(): Observable<Operation<Message>> {
        return apiRequestService.loadMessage()
            .map { Operation.Completed<Message>(it) as Operation<Message> }
            .startWith(Operation.InProgress<Message>())
            .onErrorReturn {
                Operation.Failed<Message>(
                    it
                )
            }
    }

    private fun loadResults(
        skip: Int,
        take: Int
    ): Observable<Operation<Collection<ResolutionItem>>> {
        if (sessionService.getCurrentUser() != null) {
            return apiRequestService.loadResults(skip, take)
                .map { Operation.Completed<Collection<ResolutionItem>>(it) as Operation<Collection<ResolutionItem>> }
                .startWith(Operation.InProgress<Collection<ResolutionItem>>())
                .onErrorReturn {
                    Operation.Failed<Collection<ResolutionItem>>(
                        it
                    )
                }
        } else {
            throw Exception("User is Null")
        }
    }

    private fun confirm(text: String): Observable<Operation<Unit>>
    {
        return apiRequestService.sendMessage(text)
            .map { Operation.Completed<Unit>(it) as Operation<Unit> }
            .startWith(Operation.InProgress<Unit>())
            .onErrorReturn {
                Operation.Failed<Unit>(it)
            }
    }
}

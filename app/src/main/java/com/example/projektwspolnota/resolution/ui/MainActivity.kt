package com.example.projektwspolnota.resolution.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.view.View
import android.widget.Toast
import com.example.projektwspolnota.R
import com.example.projektwspolnota.resolution.mvi.*
import com.example.projektwspolnota.services.ApiRequestService
import com.example.projektwspolnota.resolution.mvi.ResolutionItem
import com.example.projektwspolnota.results.ui.ResultsActivity
import com.example.projektwspolnota.services.SessionService
import com.example.projektwspolnota.vote.ui.VoteActivity
import com.hannesdorfmann.mosby3.mvi.MviActivity
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : MviActivity<ResolutionView, ResolutionViewPresenter>(), ResolutionView {
    override fun createPresenter(): ResolutionViewPresenter {
        return ResolutionViewPresenter(ResolutionComponentUseCase(ApiRequestService(), SessionService))
    }

    override val initIntent: Observable<Unit> = Observable.just(Unit)

    override val loadNextIntent: Observable<LoadNextPageParam>
        get() = RxRecyclerView.scrollEvents(resolutionResultList)
            .filter { it.view().canScrollVertically(1) == false && it.view().adapter?.itemCount != 0 }
            .map { event ->
                val recycleView = event.view()
                LoadNextPageParam(recycleView.adapter?.itemCount ?: 0, 5)
            }.debounce(
                500,
                TimeUnit.MILLISECONDS
            )

    override val logOutIntent: Observable<Unit>
        get() = logOut.clicks()

    override val sendMessageIntent: Observable<Unit>
        get() = sendMessage.clicks()

    override val confirmIntent: Observable<String>
        get() = confirmButton.clicks()
            .map {
                messageEditText.text.toString()
            }

    override val denyIntent: Observable<Unit>
        get() = denyButton.clicks()

    override fun render(state: ResolutionState) {
        if (state.isLoading) {
            rotateLoading.start()
            rotateLoading.visibility = View.VISIBLE
            if(SessionService.getCurrentUser()!!.isAdministrator) {
                sendMessage.visibility = View.VISIBLE
            }
        } else {
            rotateLoading.visibility = View.GONE
            rotateLoading.stop()
            resolutionAdapter.load(state.resolutionCollection)
            resultAdapter.load(state.resultCollection)
        }
        if (state.message.text != "") {
            Information.text = state.message.text
            messageDate.text = state.message.date + ":"
        } else
            Information.text =
                "Witaj ${SessionService.getCurrentUser()?.firstName} ${SessionService.getCurrentUser()?.lastName}"
        if (state.isMessageVisible) {
            Information.visibility = View.VISIBLE
        } else {
            Information.visibility = View.GONE
        }
        if (state.isResolutionListVisible) {
            resolutionList.visibility = View.VISIBLE
        } else {
            resolutionList.visibility = View.GONE
        }
        if (state.isResultListVisible) {
            resolutionResultList.visibility = View.VISIBLE
        } else {
            resolutionResultList.visibility = View.GONE
        }
        if (state.isConfirmLayoutVisible){
            confirmLayout.visibility = View.VISIBLE
        } else {
            confirmLayout.visibility = View.GONE
            messageEditText.text.clear()
        }
        if (state.isRefreshing){
            finish()
            startActivity(intent)
        }
        if (state.isLoadingNextPage) {
            rotateLoadingLoadNext.start()
            rotateLoadingLoadNext.visibility = View.VISIBLE
        } else {
            rotateLoadingLoadNext.stop()
            rotateLoadingLoadNext.visibility = View.GONE
            resultAdapter.load(state.resultCollection)
        }
        if (state.isLogingOut){
            finish()
        }
        if (state.errorMessage != null) {
            Toast.makeText(this, state.errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private lateinit var resolutionAdapter: ResolutionRecyclerViewAdapter
    private lateinit var resultAdapter: ResolutionResultsRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerViews(resolutionList, resolutionResultList)
    }

    private fun listenerResolutionAdapter(resolutionItem: ResolutionItem) {
        val intent = Intent(this@MainActivity, VoteActivity::class.java)
        intent.putExtra("resolutionItemId", resolutionItem.resolutionId)
        startActivity(intent)
        finish()
    }

    private fun listenerResultAdapter(resolutionItem: ResolutionItem) {
        val intent = Intent(this@MainActivity, ResultsActivity::class.java)
        intent.putExtra("resolutionItemId", resolutionItem.resolutionId)
        startActivity(intent)
        finish()
    }

    private fun setupRecyclerViews(resolutionRecyclerView: RecyclerView, resultRecyclerView: RecyclerView) {
        resolutionAdapter =
            ResolutionRecyclerViewAdapter(this::listenerResolutionAdapter)
        resolutionRecyclerView.adapter = resolutionAdapter
        resultAdapter =
            ResolutionResultsRecyclerViewAdapter(this::listenerResultAdapter)
        resultRecyclerView.adapter = resultAdapter
    }
}
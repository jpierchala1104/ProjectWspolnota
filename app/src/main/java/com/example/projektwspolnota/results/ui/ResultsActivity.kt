package com.example.projektwspolnota.results.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.example.projektwspolnota.R
import com.example.projektwspolnota.services.ApiRequestService
import com.example.projektwspolnota.resolution.ui.MainActivity
import com.example.projektwspolnota.results.mvi.ResultsComponentUseCase
import com.example.projektwspolnota.results.mvi.ResultsState
import com.example.projektwspolnota.results.mvi.ResultsView
import com.example.projektwspolnota.results.mvi.ResultsViewPresenter
import com.example.projektwspolnota.services.SessionService
import com.hannesdorfmann.mosby3.mvi.MviActivity
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_results.*

class ResultsActivity : MviActivity<ResultsView, ResultsViewPresenter>(), ResultsView {

    override fun createPresenter(): ResultsViewPresenter {
        return ResultsViewPresenter(ResultsComponentUseCase(ApiRequestService()))
    }

    private var resolutionId = 0

    override lateinit var initIntent: Observable<Int>

    override val exitIntent: Observable<Unit>
        get() = exitButton.clicks()

    override fun render(state: ResultsState) {
        if (state.isLoading) {
            rotateLoading.start()
            rotateLoading.visibility = View.VISIBLE
        } else {
            rotateLoading.visibility = View.GONE
            rotateLoading.stop()
        }
        if (state.isResultsListLoading == false) {
            resolutionTitleTextView.text = state.resultsItem.title
            results_layout_date.text = state.resultsItem.date
            if(SessionService.getCurrentUser()!!.isAdministrator) {
                voteCount.text = "Głosowało - " + state.resultsItem.pointsResults.first().numberOfVotes.toString()
            }
            resultsAdapter.load(state.resultsItem.pointsResults)
        }
        if (state.isResultsListVisible) {
            resultsList.visibility = View.VISIBLE
            resolutionTitleTextView.visibility = View.VISIBLE
            results_layout_date.visibility = View.VISIBLE
            voteCount.visibility = View.VISIBLE
        } else {
            resultsList.visibility = View.GONE
            resolutionTitleTextView.visibility = View.GONE
            results_layout_date.visibility = View.GONE
            voteCount.visibility = View.GONE
        }
        if (state.isClosingActivity) {
            val intent = Intent(this@ResultsActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        if (state.errorMessage != null) {
            Toast.makeText(this, state.errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this@ResultsActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private lateinit var resultsAdapter: ResultsRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        resolutionId = intent.getIntExtra("resolutionItemId", 0)

        initIntent = Observable.just(resolutionId)

        setupRecyclerViews(resultsList)
    }

    private fun setupRecyclerViews(resultsList: RecyclerView) {
        resultsAdapter =
            ResultsRecyclerViewAdapter()

        resultsList.adapter = resultsAdapter
    }
}

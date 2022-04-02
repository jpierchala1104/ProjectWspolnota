package com.example.projektwspolnota.vote.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.example.projektwspolnota.R
import com.example.projektwspolnota.vote.mvi.VotePointItem
import com.example.projektwspolnota.services.ApiRequestService
import com.example.projektwspolnota.resolution.ui.MainActivity
import com.example.projektwspolnota.services.SessionService
import com.example.projektwspolnota.vote.mvi.*
import com.hannesdorfmann.mosby3.mvi.MviActivity
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_vote.*

class VoteActivity : MviActivity<VoteView, VoteViewPresenter>(), VoteView {

    override fun createPresenter(): VoteViewPresenter {
        return VoteViewPresenter(VoteComponentUseCase(ApiRequestService(), SessionService))
    }

    private var resolutionId = 0

    override lateinit var initIntent: Observable<Int>

    override val sendIntent: Observable<Unit>
        get() = sendVote
            .clicks()

    override val confirmIntent: Observable<Unit>
        get() = confirmButton
            .clicks()

    override val denyIntent: Observable<Unit>
        get() = denyButton
            .clicks()

    override val exitIntent: Observable<Unit>
        get() = exitButton
            .clicks()

    override val voteIntent = PublishSubject.create<Vote>()

    override fun render(state: VoteState) {
        if (state.isLoading) {
            rotateLoading.start()
            rotateLoading.visibility = View.VISIBLE
        } else {
            rotateLoading.visibility = View.GONE
            rotateLoading.stop()
        }
        if (state.isVoteListLoading == false) {
            resolutionTitleTextView.text = state.voteItem.title
            vote_layout_date.text = state.voteItem.date
            voteAdapter.load(state.voteItem.points)
        }
        if (state.isVoteListVisible) {
            voteList.visibility = View.VISIBLE
            resolutionTitleTextView.visibility = View.VISIBLE
            vote_layout_date.visibility = View.VISIBLE
        } else {
            voteList.visibility = View.GONE
            resolutionTitleTextView.visibility = View.GONE
            vote_layout_date.visibility = View.GONE
        }
        if (state.isConfirmLayoutVisible) {
            confirmLayout.visibility = View.VISIBLE
        } else {
            confirmLayout.visibility = View.GONE
        }
        if (state.isClosingActivity) {
            val intent = Intent(this@VoteActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        if (state.errorMessage != null) {
            Toast.makeText(this, state.errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this@VoteActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private lateinit var voteAdapter: VoteRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vote)

        resolutionId = intent.getIntExtra("resolutionItemId", 0)

        initIntent = Observable.just(resolutionId)

        setupRecyclerViews(voteList)
    }

    private fun yesButtonListenerVoteAdapter(votePointItem: VotePointItem) {
        votePointItem.pointVote = true
        voteIntent.onNext(Vote(votePointItem.number, true))
    }

    private fun noButtonListenerVoteAdapter(votePointItem: VotePointItem) {
        votePointItem.pointVote = false
        voteIntent.onNext(Vote(votePointItem.number, false))
    }

    private fun abstainButtonListenerVoteAdapter(votePointItem: VotePointItem) {
        votePointItem.pointVote = null
        voteIntent.onNext(Vote(votePointItem.number, null))
    }

    private fun setupRecyclerViews(voteList: RecyclerView) {
        voteAdapter =
            VoteRecyclerViewAdapter(
                this::yesButtonListenerVoteAdapter,
                this::noButtonListenerVoteAdapter,
                this::abstainButtonListenerVoteAdapter
            )
        voteList.adapter = voteAdapter
    }
}

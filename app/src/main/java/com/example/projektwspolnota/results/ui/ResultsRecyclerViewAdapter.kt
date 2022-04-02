package com.example.projektwspolnota.results.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.example.projektwspolnota.R
import com.example.projektwspolnota.results.mvi.ResultsPointItem
import kotlinx.android.synthetic.main.results_list_item.view.*
import kotlinx.android.synthetic.main.vote_list_item.view.pointNumber
import kotlinx.android.synthetic.main.vote_list_item.view.resolutionText

class ResultsRecyclerViewAdapter :
    RecyclerView.Adapter<ResultsRecyclerViewAdapter.ViewHolder>() {

    private val values: ArrayList<ResultsPointItem> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.results_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.pointId.text = "Punkt ${item.number}"
        holder.pointText.text = item.text
        holder.yesProgressBar.progress = item.yesVotePercentage
        holder.noProgressBar.progress = item.noVotePercentage
        holder.abstainProgressBar.progress = item.abstainPercentage
        holder.yesTextView.text = "Na TAK zagłosowało - ${item.yesVotePercentage}%"
        holder.noTextView.text = "Na NIE zagłosowało - ${item.noVotePercentage}%"
        holder.abstainTextView.text = "Wstrzymało się - ${item.abstainPercentage}%"
    }

    override fun getItemCount() = values.size

    fun load(resultList: Collection<ResultsPointItem>) {
        values.clear()
        values.addAll(resultList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val pointId: TextView = view.pointNumber
        val pointText: TextView = view.resolutionText
        val yesProgressBar: ProgressBar = view.yes_percentage_progressBar
        val noProgressBar: ProgressBar = view.no_percentage_progressBar
        val abstainProgressBar: ProgressBar = view.abstain_percentage_progressBar
        val yesTextView: TextView = view.yes_percentage_textView
        val noTextView: TextView = view.no_percentage_textView
        val abstainTextView: TextView = view.abstain_percentage_textView
    }
}
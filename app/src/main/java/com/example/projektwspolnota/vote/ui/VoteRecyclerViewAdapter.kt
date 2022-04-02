package com.example.projektwspolnota.vote.ui

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.projektwspolnota.R
import com.example.projektwspolnota.vote.mvi.VotePointItem
import kotlinx.android.synthetic.main.vote_list_item.view.*

class VoteRecyclerViewAdapter(
    private val yesButtonListener: (VotePointItem) -> Unit,
    private val noButtonListener: (VotePointItem) -> Unit,
    private val abstainButtonListener: (VotePointItem) -> Unit
) :
    RecyclerView.Adapter<VoteRecyclerViewAdapter.ViewHolder>() {

    private val values: ArrayList<VotePointItem> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.vote_list_item, parent, false)
        val viewHolder = ViewHolder(view)


        view.voteYes.setOnClickListener { clickListener(values[viewHolder.adapterPosition], "Yes") }
        view.voteNo.setOnClickListener { clickListener(values[viewHolder.adapterPosition], "No") }
        view.abstain.setOnClickListener { clickListener(values[viewHolder.adapterPosition], "Abstain") }
        return viewHolder
    }

    private fun clickListener(votePointItem: VotePointItem, button: String) {
        when (button) {
            "Yes" -> yesButtonListener(votePointItem)
            "No" -> noButtonListener(votePointItem)
            else -> abstainButtonListener(votePointItem)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.pointId.text = "Punkt ${item.number}"
        holder.pointText.text = item.text
        when (item.pointVote) {
            true -> {
                holder.abstButton.setBackgroundColor(Color.rgb(197,200,217))
                holder.yesButton.setBackgroundColor(Color.rgb(89,121,79))
                holder.noButton.setBackgroundColor(Color.rgb(227, 56, 38))
            }
            false -> {
                holder.abstButton.setBackgroundColor(Color.rgb(197,200,217))
                holder.noButton.setBackgroundColor(Color.rgb(181,44,30))
                holder.yesButton.setBackgroundColor(Color.rgb(112, 152, 99))
            }
            null -> {
                holder.abstButton.setBackgroundColor(Color.rgb(147,150,167))
                holder.yesButton.setBackgroundColor(Color.rgb(112, 152, 99))
                holder.noButton.setBackgroundColor(Color.rgb(227, 56, 38))
            }
        }
    }

    override fun getItemCount() = values.size

    fun getValues() = values

    fun load(resultList: Collection<VotePointItem>) {
        values.clear()
        values.addAll(resultList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val pointId: TextView = view.pointNumber
        val pointText: TextView = view.resolutionText
        val yesButton: Button = view.voteYes
        val noButton: Button = view.voteNo
        val abstButton: Button = view.abstain
    }
}
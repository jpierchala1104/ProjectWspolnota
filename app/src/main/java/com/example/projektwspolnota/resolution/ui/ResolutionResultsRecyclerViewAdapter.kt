package com.example.projektwspolnota.resolution.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.projektwspolnota.R
import com.example.projektwspolnota.resolution.mvi.ResolutionItem
import kotlinx.android.synthetic.main.resolution_list_item.view.resolutionTitle
import kotlinx.android.synthetic.main.resolution_list_item.view.*

class ResolutionResultsRecyclerViewAdapter(
    private val listener:(ResolutionItem)->Unit
) :
    RecyclerView.Adapter<ResolutionResultsRecyclerViewAdapter.ViewHolder>() {

    private val values: ArrayList<ResolutionItem> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.resolution_list_item, parent, false)
        val viewHolder = ViewHolder(view)

        view.setOnClickListener { clickListener(values[viewHolder.adapterPosition]) }
        return viewHolder
    }
    private fun clickListener(resolutionItem: ResolutionItem) {
        listener(resolutionItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.resolutionTitleView.text = item.title
        holder.dateView.text = "Uchwała z dnia - ${item.date}"
    }

    override fun getItemCount() = values.size

    fun load(resultList: Collection<ResolutionItem>) {
        values.clear()
        values.addAll(resultList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val resolutionTitleView: TextView = view.resolutionTitle
        val dateView: TextView = view.date
    }
}
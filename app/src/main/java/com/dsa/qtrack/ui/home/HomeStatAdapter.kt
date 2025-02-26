package com.dsa.qtrack.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dsa.qtrack.R

class HomeStatAdapter(private var stats: List<HomeStat>) : RecyclerView.Adapter<HomeStatAdapter.StatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_stat_card, parent, false)
        return StatViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatViewHolder, position: Int) {
        val stat = stats[position]
        holder.tvCount.text = stat.count
        holder.tvTitle.text = stat.title
    }

    override fun getItemCount() = stats.size

    fun updateStats(newStats: List<HomeStat>) {
        stats = newStats
        notifyDataSetChanged()
    }

    inner class StatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCount: TextView = view.findViewById(R.id.tvCount)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
    }
}

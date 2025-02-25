package com.dsa.qtrack.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.dsa.qtrack.R

class HomeCardAdapter(private val cards: List<HomeCard>) : RecyclerView.Adapter<HomeCardAdapter.HomeCardViewHolder>() {

    inner class HomeCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.cardView)
        val title: TextView = itemView.findViewById(R.id.cardTitle)
        val count: TextView = itemView.findViewById(R.id.cardCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeCardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_card, parent, false)
        return HomeCardViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeCardViewHolder, position: Int) {
        val card = cards[position]
        holder.title.text = card.title
        holder.count.text = card.count.toString()
        holder.cardView.setCardBackgroundColor(holder.itemView.context.getColor(card.color))
    }

    override fun getItemCount(): Int = cards.size
}

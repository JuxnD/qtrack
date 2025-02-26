package com.dsa.qtrack.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dsa.qtrack.R

class HomeMenuAdapter(private val menuItems: List<HomeMenuItem>) : RecyclerView.Adapter<HomeMenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu_card, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val item = menuItems[position]
        holder.tvTitle.text = item.title
        holder.ivIcon.setImageResource(item.iconRes)
        holder.itemView.setOnClickListener { item.action() }
    }

    override fun getItemCount() = menuItems.size

    inner class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivIcon: ImageView = view.findViewById(R.id.ivIcon)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
    }
}

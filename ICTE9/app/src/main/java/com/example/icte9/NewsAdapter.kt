package com.example.icte9

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NewsAdapter(private var items: List<String>) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    // ViewHolder class that holds the reference to the UI element for each item
    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val headline: TextView = itemView.findViewById(R.id.headline_text)
    }

    // Called when RecyclerView needs a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_headline, parent, false)
        return NewsViewHolder(view)
    }

    // Binds data to each item view
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.headline.text = items[position]
    }

    // Returns the total number of items
    override fun getItemCount(): Int = items.size

    // Updates the list of items and refreshes the RecyclerView
    fun updateData(newItems: List<String>) {
        items = newItems
        notifyDataSetChanged()
    }
}

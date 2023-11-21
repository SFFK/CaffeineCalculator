package com.cookandroid.caffeinecalculator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotifyAdapter(val data : ArrayList<NotifyItem>) : RecyclerView.Adapter<NotifyAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtNotify.text = data[position].title
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNotify = itemView.findViewById<TextView>(R.id.notifyText1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notify_recylerview, parent, false)
        return ViewHolder(view)
    }
}
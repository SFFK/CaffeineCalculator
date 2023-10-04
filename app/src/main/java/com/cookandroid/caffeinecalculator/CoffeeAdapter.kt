package com.cookandroid.caffeinecalculator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class CoffeeAdapter(val data : MutableList<String>) : RecyclerView.Adapter<CoffeeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.coffee_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtBrand.text = data[position]
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtBrand = itemView.findViewById<TextView>(R.id.item_content)
    }
}
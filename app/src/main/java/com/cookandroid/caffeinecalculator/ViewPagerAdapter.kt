package com.cookandroid.caffeinecalculator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_viewpager.view.*

class ViewPagerAdapter(itemList : ArrayList<Int>) : RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {
    var item = itemList

    override fun getItemCount() : Int {
        return item.size
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : PagerViewHolder {
        return PagerViewHolder(parent)
    }

    override fun onBindViewHolder(holder : PagerViewHolder, position : Int) {
        holder.item.setImageResource(item[position])
    }

    inner class PagerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
        (LayoutInflater.from(parent.context).inflate(R.layout.item_viewpager, parent, false)) {

            val item = itemView.images!!
    }
}

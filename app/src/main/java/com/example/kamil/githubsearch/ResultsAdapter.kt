package com.example.kamil.githubsearch

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Kamil on 29.06.2018.
 */
class ResultsAdapter : RecyclerView.Adapter<CustomViewHolder>() {

    override fun getItemCount(): Int {
        return 6;
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.item, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

    }

}

class CustomViewHolder(v : View) : RecyclerView.ViewHolder(v) {

}
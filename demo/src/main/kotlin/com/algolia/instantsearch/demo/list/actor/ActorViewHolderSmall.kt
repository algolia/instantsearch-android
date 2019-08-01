package com.algolia.instantsearch.demo.list.actor

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_small.view.*


class ActorViewHolderSmall(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(actor: Actor) {
        view.itemName.text = actor.name
    }
}
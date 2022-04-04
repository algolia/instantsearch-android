package com.algolia.instantsearch.examples.showcase.view.list.actor

import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.examples.databinding.ListItemSmallBinding

class ActorViewHolderSmall(val binding: ListItemSmallBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(actor: Actor) {
        binding.itemName.text = actor.name
    }
}

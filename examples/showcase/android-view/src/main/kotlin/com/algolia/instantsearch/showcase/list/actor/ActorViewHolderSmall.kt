package com.algolia.instantsearch.showcase.list.actor

import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.showcase.databinding.ListItemSmallBinding

class ActorViewHolderSmall(val binding: ListItemSmallBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(actor: Actor) {
        binding.itemName.text = actor.name
    }
}

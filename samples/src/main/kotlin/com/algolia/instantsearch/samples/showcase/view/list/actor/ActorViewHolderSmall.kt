package com.algolia.instantsearch.samples.showcase.view.list.actor

import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.samples.databinding.ListItemSmallBinding

class ActorViewHolderSmall(val binding: ListItemSmallBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(actor: Actor) {
        binding.itemName.text = actor.name
    }
}

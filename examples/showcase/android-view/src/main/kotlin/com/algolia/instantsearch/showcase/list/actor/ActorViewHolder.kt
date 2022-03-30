package com.algolia.instantsearch.showcase.list.actor

import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.showcase.databinding.ListItemActorBinding

class ActorViewHolder(private val binding: ListItemActorBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(actor: Actor) {
        binding.actorName.text = actor.name
    }
}

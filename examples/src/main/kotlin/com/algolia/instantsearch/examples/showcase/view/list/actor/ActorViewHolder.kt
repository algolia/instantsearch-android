package com.algolia.instantsearch.examples.showcase.view.list.actor

import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.examples.databinding.ListItemActorBinding

class ActorViewHolder(private val binding: ListItemActorBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(actor: Actor) {
        binding.actorName.text = actor.name
    }
}

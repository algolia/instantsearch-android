package com.algolia.instantsearch.showcase.list.merged

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.showcase.databinding.ListItemSmallBinding
import com.algolia.instantsearch.showcase.layoutInflater
import com.algolia.instantsearch.showcase.list.actor.Actor
import com.algolia.instantsearch.showcase.list.actor.ActorViewHolderSmall

class ActorsAdapter : ListAdapter<Actor, ActorViewHolderSmall>(ActorsAdapter), HitsView<Actor> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolderSmall {
        return ActorViewHolderSmall(
            ListItemSmallBinding.inflate(parent.layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ActorViewHolderSmall, position: Int) {
        val product = getItem(position)
        if (product != null) holder.bind(product)
    }

    companion object : DiffUtil.ItemCallback<Actor>() {

        override fun areItemsTheSame(
            oldItem: Actor,
            newItem: Actor
        ): Boolean {
            return oldItem.objectID == newItem.objectID
        }

        override fun areContentsTheSame(
            oldItem: Actor,
            newItem: Actor
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun setHits(hits: List<Actor>) {
        submitList(hits)
    }
}

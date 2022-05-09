package com.algolia.instantsearch.examples.android.showcase.androidview.list.actor

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.algolia.instantsearch.examples.android.databinding.ListItemActorBinding
import com.algolia.instantsearch.examples.android.showcase.androidview.layoutInflater

class ActorAdapterNested : PagingDataAdapter<Actor, ActorViewHolder>(this) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
        return ActorViewHolder(
            ListItemActorBinding.inflate(parent.layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        val item = getItem(position)

        if (item != null) holder.bind(item)
    }

    private companion object DiffUtilActor : DiffUtil.ItemCallback<Actor>() {

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
}

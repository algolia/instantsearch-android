package com.algolia.instantsearch.demo.list.actor

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.inflate


class ActorAdapterNested : PagedListAdapter<Actor, ActorViewHolderNested>(this) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolderNested {
        return ActorViewHolderNested(parent.inflate(R.layout.list_item_actor))
    }

    override fun onBindViewHolder(holder: ActorViewHolderNested, position: Int) {
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
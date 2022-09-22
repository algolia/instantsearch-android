package com.algolia.instantsearch.examples.android.codex.multisearch

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.android.highlighting.toSpannedString
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.examples.android.databinding.ListItemSmallBinding
import com.algolia.instantsearch.examples.android.showcase.androidview.layoutInflater

class ActorAdapter : ListAdapter<Actor, ActorViewHolder>(ActorDiffUtil), HitsView<Actor> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
        return ActorViewHolder(
            ListItemSmallBinding.inflate(parent.layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        val item = getItem(position)

        if (item != null) holder.bind(item)
    }

    override fun setHits(hits: List<Actor>) {
        submitList(hits)
    }
}

class ActorViewHolder(private val binding: ListItemSmallBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(actor: Actor) {
        binding.itemName.text = actor.highlightedName?.toSpannedString() ?: actor.name
    }
}

object ActorDiffUtil : DiffUtil.ItemCallback<Actor>() {
    override fun areItemsTheSame(oldItem: Actor, newItem: Actor) = oldItem.objectID == newItem.objectID
    override fun areContentsTheSame(oldItem: Actor, newItem: Actor) = oldItem == newItem
}

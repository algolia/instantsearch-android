package com.algolia.instantsearch.android.sample

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.algolia.search.model.Attribute
import com.algolia.search.model.response.ResponseSearch
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.hit_item.view.*
import kotlinx.serialization.json.content

class HitsAdapter(
    var hits: List<ResponseSearch.Hit>
) : RecyclerView.Adapter<HitsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.hit_item,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = hits.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(hits[position])


    inner class ViewHolder(private var view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        override fun onClick(v: View?) {
            Log.d("HVH", "click!")
        }

        init {
            view.setOnClickListener(this)
        }

        fun bind(hit: ResponseSearch.Hit) {
            view.nameView.text = hit.getValue("name").content
            view.brandView.text = view.context.getString(R.string.brand_template).format(hit.getValue("brand").content)
            view.priceView.text =
                view.context.getString(R.string.price_template).format(hit.getValue("price").primitive.int)
            view.materialView.text = hit.getValue("material").content
            view.colorView.text = hit.getValue("color").content

            view.categoryView.text = hit.getHierarchy(Attribute("categories"))
                .getValue("lvl1")
                .joinToString(" | ") { it.replace("products > ", "") }

            Glide.with(view.context)
                .load(hit.getValue("image").content)
                .placeholder(android.R.drawable.ic_menu_help)
                .error(android.R.color.holo_red_light)
                .into(view.imageView)
        }
    }

}


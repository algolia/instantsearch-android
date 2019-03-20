package com.algolia.instantsearch.android.sample

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.android.StatsWidget
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.response.ResponseSearch.Hit
import com.algolia.search.model.search.Query
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.hit_item.view.*
import kotlinx.serialization.json.content
import searcher.SearcherSingleIndex


class MainActivity : AppCompatActivity() {

    private val hits = mutableListOf<Hit>()
    private lateinit var adapter: HitsAdapter

    //TODO: SingleIndexActivity, MultiIndexActivity, RefinementActivity
    //TODO: When moving to RefinementActivity, don't trigger a new request to display results
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup algolia
        val client = ClientSearch(ApplicationID("latency"), APIKey("3cfd1f8bfa88c7709f6bacf8203194e8"))
        val index = client.initIndex(IndexName("products_android_demo"))
        val searcher = SearcherSingleIndex(index, Query(""))

        // Prepare widgets
        adapter = HitsAdapter(hits)
        hitsView.adapter = HitsAdapter(hits)
        val layoutManager = LinearLayoutManager(this)
        hitsView.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        hitsView.addItemDecoration(dividerItemDecoration)
        val stats = StatsWidget(statsView)

        searchBox.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searcher.query.query = newText
                searcher.search()
                return true
            }
        })

        // Setup and trigger search
        searcher.listeners += {
            stats.updateView(it)
            hits.clear()
            hits.addAll(it.hits)
            adapter.notifyDataSetChanged()
        }
        searcher.search()
    }
}

class HitsAdapter(
    private val hits: List<Hit>
) : RecyclerView.Adapter<HitViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HitViewHolder =
        HitViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.hit_item, parent, false))

    override fun getItemCount(): Int = hits.size

    override fun onBindViewHolder(holder: HitViewHolder, position: Int) = holder.bind(hits[position])
}


class HitViewHolder(private var view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {


    override fun onClick(v: View?) {
        Log.d("HVH", "click!")
    }

    init {
        view.setOnClickListener(this)
    }

    fun bind(hit: Hit) {
        view.nameView.text = hit.json["name"].content
        view.brandView.text = hit.json["brand"].content
        view.priceView.text = "$ " + hit.json["price"].content
        view.materialView.text = hit.json["material"].content
        view.colorView.text = hit.json["color"].content
        view.categoryView.text = hit.getHierarchy(Attribute("categories")).getValue("lvl1").reduce { acc, category ->
            acc + category.replace("products >", "")
        }
    }
}

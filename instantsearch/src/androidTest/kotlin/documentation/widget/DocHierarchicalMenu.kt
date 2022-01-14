package documentation.widget

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.tree.connectView
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.hierarchical.HierarchicalItem
import com.algolia.instantsearch.helper.hierarchical.HierarchicalPresenterImpl
import com.algolia.instantsearch.helper.hierarchical.HierarchicalView
import com.algolia.instantsearch.helper.hierarchical.HierarchicalViewModel
import com.algolia.instantsearch.helper.hierarchical.connectFilterState
import com.algolia.instantsearch.helper.hierarchical.connectSearcher
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.instantsearch.helper.searcher.hits.HitsSearcher
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import org.junit.Ignore

@Ignore
internal class DocHierarchicalMenu {

    class HierarchicalViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        @Suppress("UNUSED_PARAMETER")
        fun bind(item: HierarchicalItem, onClick: View.OnClickListener) {
            // Bind your view
        }
    }

    class HierarchicalAdapter : ListAdapter<HierarchicalItem, HierarchicalViewHolder>(diffUtil), HierarchicalView {

        override var onSelectionChanged: Callback<String>? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HierarchicalViewHolder {
            return HierarchicalViewHolder(View(parent.context))
        }

        override fun onBindViewHolder(holder: HierarchicalViewHolder, position: Int) {
            val item = getItem(position)

            holder.bind(item, View.OnClickListener { onSelectionChanged?.invoke(item.facet.value) })
        }

        override fun setTree(tree: List<HierarchicalItem>) {
            submitList(tree)
        }

        companion object {

            private val diffUtil = object : DiffUtil.ItemCallback<HierarchicalItem>() {

                override fun areItemsTheSame(oldItem: HierarchicalItem, newItem: HierarchicalItem): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(oldItem: HierarchicalItem, newItem: HierarchicalItem): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }

    class MyActivity : AppCompatActivity() {

        val client = ClientSearch(
            ApplicationID("YourApplicationID"),
            APIKey("YourAPIKey")
        )
        val searcher = HitsSearcher(client, IndexName("YourIndexName"))
        val filterState = FilterState()

        val hierarchicalCategory = Attribute("hierarchicalCategories")
        val hierarchicalCategoryLvl0 = Attribute("$hierarchicalCategory.lvl0")
        val hierarchicalCategoryLvl1 = Attribute("$hierarchicalCategory.lvl1")
        val hierarchicalCategoryLvl2 = Attribute("$hierarchicalCategory.lvl2")
        val hierarchicalAttributes = listOf(
            hierarchicalCategoryLvl0,
            hierarchicalCategoryLvl1,
            hierarchicalCategoryLvl2
        )
        val separator = " > "
        val viewModel = HierarchicalViewModel(hierarchicalCategory, hierarchicalAttributes, separator)
        val connection = ConnectionHandler()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            val view = HierarchicalAdapter()

            connection += searcher.connectFilterState(filterState)
            connection += viewModel.connectFilterState(filterState)
            connection += viewModel.connectSearcher(searcher)
            connection += viewModel.connectView(view, HierarchicalPresenterImpl(separator))

            searcher.searchAsync()
        }

        override fun onDestroy() {
            super.onDestroy()
            searcher.cancel()
            connection.disconnect()
        }
    }
}

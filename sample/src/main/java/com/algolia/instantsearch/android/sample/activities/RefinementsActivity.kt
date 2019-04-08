package com.algolia.instantsearch.android.sample.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.android.Stats
import com.algolia.instantsearch.android.sample.R
import com.algolia.instantsearch.android.sample.views.OneValueToggle
import com.algolia.instantsearch.android.sample.views.RefinementList
import com.algolia.search.model.Attribute
import kotlinx.android.synthetic.main.activity_refinements.*

open class RefinementsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refinements)

        val searcher = initSearcherSingleIndex()
        addDebugListeners(searcher)

//        val list = RefinementList(recyclerView)
//        list.connectWithSearcher(searcher, Attribute("brand"))

        prepareWidgets(
            searcher, listOf(OneValueToggle(checkedTextView, Attribute("brand"), "Chanel"), Stats(statsView))
        )

        searcher.search()
    }
}

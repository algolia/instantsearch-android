package com.algolia.instantsearch.examples.android.guides.gettingstarted

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.algolia.instantsearch.examples.android.R

class GettingStartedGuide : AppCompatActivity() {

    val viewModel: MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getting_started)
        showProductFragment()
        setupNavigation()
    }

    private fun showProductFragment() {
        supportFragmentManager.commit {
            replace<ProductFragment>(R.id.container)
        }
    }

    private fun setupNavigation() {
        viewModel.displayFilters.observe(this) {
            showFacetFragment()
        }
    }

    private fun showFacetFragment() {
        supportFragmentManager.commit {
            add<FacetFragment>(R.id.container)
            addToBackStack("facet")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        if (supportFragmentManager.popBackStackImmediate()) return true
        return super.onSupportNavigateUp()
    }
}

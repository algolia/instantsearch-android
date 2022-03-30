package com.algolia.instantsearch.guides.gettingstarted

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.algolia.instantsearch.guides.R

class GettingStartedGuide : AppCompatActivity() {

    val viewModel: MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getting_started)
        setupNavigation()
        showProductFragment()
    }

    private fun setupNavigation() {
        viewModel.displayFilters.observe(this) {
            showFacetFragment()
        }
    }

    private fun showFacetFragment() {
        supportFragmentManager.commit {
            replace<FacetFragment>(R.id.container)
            addToBackStack("facet")
        }
    }

    private fun showProductFragment() {
        supportFragmentManager.commit {
            replace<ProductFragment>(R.id.container)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        if (supportFragmentManager.popBackStackImmediate()) return true
        return super.onSupportNavigateUp()
    }
}

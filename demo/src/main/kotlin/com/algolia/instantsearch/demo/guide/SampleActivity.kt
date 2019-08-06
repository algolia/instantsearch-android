package com.algolia.instantsearch.demo.guide

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.demo.R


class SampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sample_activity)

        showProductFragment()
    }

    fun showFacetFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.container, FacetFragment())
            .addToBackStack("facet")
            .commit()
    }

    fun showProductFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.container, ProductFragment())
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        supportFragmentManager.popBackStack()
        return super.onSupportNavigateUp()
    }
}
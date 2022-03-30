package com.algolia.instantsearch.showcase.customdata

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.showcase.databinding.ShowcaseQueryRuleCustomDataTemplateBinding

class TemplateActivity : AppCompatActivity() {

    private lateinit var binding: ShowcaseQueryRuleCustomDataTemplateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ShowcaseQueryRuleCustomDataTemplateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        val text = intent.getStringExtra(EXTRA_CONTENT)
        binding.content.text = text
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            it.title = null
            it.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_CONTENT = "TEMPLATE_CONTENT"
    }
}

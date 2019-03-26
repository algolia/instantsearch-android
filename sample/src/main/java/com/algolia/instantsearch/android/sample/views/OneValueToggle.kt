package com.algolia.instantsearch.android.sample.views

import android.R
import android.graphics.drawable.Drawable
import android.text.SpannedString
import android.widget.CheckedTextView
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import com.algolia.instantsearch.android.sample.activities.toast
import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Facet
import refinement.RefinementView

class OneValueToggle constructor(
    val view: CheckedTextView,
    val attribute: Attribute, val value: String
) : RefinementView<Facet> {

    private var facet: Facet? = null

    private var displayAttribute = false

    private val checkedDrawable: Drawable = view.checkMarkDrawable

    init {
        view.post {
            updateView()
        }
    }


    override fun setOnClickRefinement(onClick: (Facet?) -> Unit) {
        view.setOnClickListener {
            if (facet != null) facet = null else facet = Facet(value, 0)
            updateView()
            onClick(facet)
        }
    }

    override fun setRefinements(refinements: List<Facet>) {
        facet = refinements.find { it.name == value }

        updateView()
    }

    override fun setSelected(refinements: List<Facet>) {
        facet = refinements.firstOrNull { it.name == value }
        updateView()
    }

    private fun updateView(facet: Facet? = this.facet) {
        view.isChecked = if (facet != null) {
            view.checkMarkDrawable = checkedDrawable
            true
        } else {
            view.checkMarkDrawable = view.context.getDrawable(R.drawable.checkbox_off_background)
            false
        }
        view.isChecked = (facet != null)
        view.text = buildText()
    }

    private fun buildText(facet: Facet? = this.facet): SpannedString {
        return buildSpannedString {
            if (displayAttribute) append("${attribute.raw}: ")
            append(value)
            if (facet != null) {
                append(" ")
                bold { "${facet.count}" }
            }
        }
    }
}

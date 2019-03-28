package com.algolia.instantsearch.android.sample.views

import android.R
import android.graphics.drawable.Drawable
import android.text.SpannedString
import android.widget.CheckedTextView
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Facet
import refinement.RefinementView

class OneValueToggle constructor(
    val view: CheckedTextView,
    val attribute: Attribute,
    var value: String
) : RefinementView<Facet> {
    private var data = Facet(value, 0)

    private var displayAttribute = false

    private val checkedDrawable: Drawable = view.checkMarkDrawable
    init {
        view.post {
            updateView()
        }
    }

    override fun setOnClickRefinement(onClick: (Facet, Boolean) -> Unit) {
        view.setOnClickListener {
            view.toggle()
            updateView()
            onClick(data, view.isChecked)
        }
    }

    override fun setRefinements(refinements: List<Facet>) {
        data = refinements.find { it.name == value } ?: Facet(value, 0)
        updateView()
    }

    override fun setSelected(refinements: List<Facet>) {
        data = refinements.firstOrNull { it.name == value } ?: Facet(value, 0)
        updateView()
    }

    private fun updateView() {
        if (view.isChecked) {
            view.checkMarkDrawable = checkedDrawable
        } else {
            view.checkMarkDrawable = view.context.getDrawable(R.drawable.checkbox_off_background)
        }
        view.text = buildText()
    }

    private fun buildText(facet: Facet = this.data): SpannedString {
        return buildSpannedString {
            if (displayAttribute) append("${attribute.raw}: ")
            append(value)
            if (facet.count != 0) {
                append(" ")
                bold { "${facet.count}" }
            }
        }
    }
}

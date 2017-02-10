package com.algolia.instantsearch.ui.views.filters;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.CompoundButton;

import com.algolia.instantsearch.R;
import com.algolia.instantsearch.model.SearchResults;

/**
 * A widget that toggles between refining and not refining an attribute with a given value.
 */
public class OneValueToggle extends Toggle implements AlgoliaFacetFilter {
    /** The value to apply when the OneValueToggle is checked. */
    public String value;

    public OneValueToggle(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.OneValueToggle, 0, 0);
        try {
            value = styledAttributes.getString(R.styleable.OneValueToggle_value);
        } finally {
            styledAttributes.recycle();
        }
    }


    @Override protected OnCheckedChangeListener getOnCheckedChangeListener() {
        return new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                searcher.updateFacetRefinement(attributeName, value, isChecked).search();
            }
        };
    }

    @Override protected String applyTemplate(SearchResults results) {
        return template
                .replace("{name}", attributeName)
//FIXME                    .replace("{count}", String.valueOf(results.facets.get(attributeName).size()))
                .replace("{isRefined}", String.valueOf(isChecked()))
                .replace("{value}", value);
    }

    @Override public void updateRefinementWithNewName(String newName) {
        if (isChecked()) { // We need to update facetRefinement's attribute
            searcher.removeFacetRefinement(attributeName, value)
                    .addFacetRefinement(newName, value)
                    .search();
        }
    }

    /**
     * Change the OneValueToggle's value, updating facet refinements accordingly.
     *
     * @param newValue the new value to refine with.
     * @param newName  an eventual new attribute name.
     */
    public void setValue(String newValue, @Nullable String newName) {
        if (isChecked()) {
            searcher.updateFacetRefinement(this.attributeName, value, false)
                    .updateFacetRefinement(newName != null ? newName : attributeName, newValue, true)
                    .search();
        }
        this.value = newValue;
        applyEventualNewName(newName);
    }

}
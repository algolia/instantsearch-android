package com.algolia.instantsearch.ui.views.filters;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.CompoundButton;

import com.algolia.instantsearch.R;
import com.algolia.instantsearch.core.events.FacetRefinementEvent;
import com.algolia.instantsearch.core.model.SearchResults;

import org.greenrobot.eventbus.Subscribe;

import static com.algolia.instantsearch.core.events.RefinementEvent.Operation.ADD;

/**
 * Toggles between refining and not refining an attribute with a given value.
 */
public class OneValueToggle extends Toggle implements AlgoliaFilter {
    /** The value to apply when the OneValueToggle is checked. */
    private String value;

    /**
     * Constructs a new OneValueToggle with the given context's theme and the supplied attribute set.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     */
    public OneValueToggle(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.OneValueToggle, 0, 0);
        try {
            value = styledAttributes.getString(R.styleable.OneValueToggle_value);
        } finally {
            styledAttributes.recycle();
        }
    }

    /**
     * Changes the OneValueToggle's value, updating facet refinements accordingly.
     *
     * @param newValue the new value to refine with.
     * @param newName  an eventual new attribute name.
     */
    public void setValue(String newValue, @Nullable String newName) {
        if (isChecked()) {
            searcher.updateFacetRefinement(this.attribute, value, false)
                    .updateFacetRefinement(newName != null ? newName : attribute, newValue, true)
                    .search();
        }
        this.value = newValue;
        applyEventualNewAttribute(newName);
    }

    @Override public void updateRefinementWithNewName(String newName) {
        if (isChecked()) { // We need to update facetRefinement's attribute
            searcher.removeFacetRefinement(attribute, value)
                    .addFacetRefinement(newName, value)
                    .search();
        }
    }

    @Override protected OnCheckedChangeListener getOnCheckedChangeListener() {
        return new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                searcher.updateFacetRefinement(attribute, value, isChecked).search();
            }
        };
    }

    @Override protected String applyTemplates(@NonNull SearchResults results) {
        return template
                .replace("{name}", attribute)
//FIXME                    .replace("{count}", String.valueOf(results.facets.get(attributeName).size()))
                .replace("{isRefined}", String.valueOf(isChecked()))
                .replace("{value}", value);
    }

    @Subscribe
    public void onFacetRefinementEvent(FacetRefinementEvent event) {
        if (event.attribute.equals(attribute) && event.value.equals(value)) {
            setChecked(event.operation == ADD);
        }
    }

}

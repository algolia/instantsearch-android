package com.algolia.instantsearch.ui.views.filters;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.CompoundButton;

import com.algolia.instantsearch.R;
import com.algolia.instantsearch.events.FacetRefinementEvent;
import com.algolia.instantsearch.model.SearchResults;

import org.greenrobot.eventbus.Subscribe;


public class TwoValuesToggle extends Toggle implements AlgoliaFacetFilter {
    /** The value to apply when the Toggle is checked. */
    private String valueOn;
    /** An eventual value to apply when the Toggle is unchecked. */
    private String valueOff;

    private boolean isRefined;

    /**
     * Constructs a new TwoValuesToggle with the given context's theme and the supplied attribute set.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     */public TwoValuesToggle(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TwoValuesToggle, 0, 0);
        try {
            valueOn = styledAttributes.getString(R.styleable.TwoValuesToggle_valueOn);
            valueOff = styledAttributes.getString(R.styleable.TwoValuesToggle_valueOff);
            if (valueOff != null) {
                isRefined = true;
            }
        } finally {
            styledAttributes.recycle();
        }
    }

    /**
     * Changes the Toggle's valueOn, updating facet refinements accordingly.
     *
     * @param newValue valueOn's new value.
     * @param newName  an eventual new attribute name.
     */
    public void setValueOn(String newValue, @Nullable String newName) {
        if (isRefined && isChecked()) { // refining on valueOn: facetRefinement needs an update
            searcher.updateFacetRefinement(attribute, valueOn, false)
                    .updateFacetRefinement(newName != null ? newName : attribute, newValue, true)
                    .search();
        }
        this.valueOn = newValue;
        applyEventualNewAttribute(newName);
    }

    /**
     * Changes the Toggle's valueOff, updating facet refinements accordingly.
     *
     * @param newValue valueOff's new value.
     * @param newName  an eventual new attribute name.
     */
    public void setValueOff(String newValue, @Nullable String newName) {
        if (isRefined) { // we may need to update facets
            if (!isChecked()) { // refining on valueOff: facetRefinement needs an update
                searcher.updateFacetRefinement(attribute, valueOff, false)
                        .updateFacetRefinement(newName != null ? newName : attribute, newValue, true)
                        .search();
            }
        } else { // now we have a valueOff, let's refine with it
            searcher.updateFacetRefinement(attribute, newValue, true).search();
            isRefined = true;
        }
        this.valueOff = newValue;
        applyEventualNewAttribute(newName);
    }

    @Override
    protected OnCheckedChangeListener getOnCheckedChangeListener() {
        return new TwoValuesToggle.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (valueOff == null) { // Apply valueOn if checked, else no refinement
                    searcher.updateFacetRefinement(attribute, valueOn, isChecked).search();
                    isRefined = isChecked;
                } else { // Toggle refined values
                    searcher.updateFacetRefinement(attribute, isChecked ? valueOn : valueOff, true)
                            .updateFacetRefinement(attribute, isChecked ? valueOff : valueOn, false)
                            .search();
                }
            }
        };
    }

    @Override protected String applyTemplates(@NonNull SearchResults results) {
        return template
                .replace("{name}", attribute)
//FIXME                    .replace("{count}", String.valueOf(results.facets.get(attributeName).size()))
                .replace("{isRefined}", String.valueOf(isRefined))
                .replace("{value}", String.valueOf(isRefined ?
                        isChecked() ? valueOn : valueOff
                        : valueOn));
    }

    @Override protected void updateRefinementWithNewName(String newName) {
        if (isRefined) { // We need to update facetRefinement's attribute
            String valueRefined = isChecked() ? valueOn : valueOff;
            searcher.removeFacetRefinement(attribute, valueRefined)
                    .addFacetRefinement(newName, valueRefined).search();
        }
    }

    @Subscribe
    public void onFacetRefinementEvent(FacetRefinementEvent event) {
    }
}
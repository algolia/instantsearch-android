package com.algolia.instantsearch.ui.views.filters;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;
import android.widget.CompoundButton;

import com.algolia.instantsearch.R;
import com.algolia.instantsearch.helpers.Searcher;
import com.algolia.instantsearch.model.SearchResults;
import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Query;


public class Toggle extends AppCompatCheckBox implements AlgoliaFacetFilter {
    /** The attribute to refine on. */
    public String attributeName;
    /** The value to apply when the Toggle is checked. */
    public String valueOn;
    /** An eventual value to apply when the Toggle is unchecked. */
    public String valueOff;

    /** True if the Toggle should hide when results are empty. */
    public boolean autoHide;

    private Searcher searcher;
    private boolean isRefined;
    private boolean shouldHide;

    public Toggle(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Toggle, 0, 0);
        try {
            attributeName = styledAttributes.getString(R.styleable.Toggle_attributeName);
            valueOn = styledAttributes.getString(R.styleable.Toggle_valueOn);
            valueOff = styledAttributes.getString(R.styleable.Toggle_valueOff);
            autoHide = styledAttributes.getBoolean(R.styleable.Toggle_autoHide, false);
            if (valueOff != null) {
                isRefined = true;
            }
        } finally {
            styledAttributes.recycle();
        }
    }

    @NonNull @Override public String getAttribute() {
        return attributeName;
    }

    @Override public void initWithSearcher(@NonNull final Searcher searcher) {
        this.searcher = searcher;

        // If we have a valueOff, refine according to checked state
        if (valueOff != null) {
            searcher.updateFacetRefinement(attributeName, isChecked() ? valueOn : valueOff, true).search();
        }

        // Setup user interaction listener
        setOnCheckedChangeListener(new Toggle.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (valueOff == null) { // Apply valueOn if checked, else no refinement
                    searcher.updateFacetRefinement(attributeName, valueOn, isChecked).search();
                    isRefined = isChecked;
                } else { // Toggle refined values
                    searcher.updateFacetRefinement(attributeName, isChecked ? valueOn : valueOff, true)
                            .updateFacetRefinement(attributeName, isChecked ? valueOff : valueOn, false)
                            .search();
                }
            }
        });
    }

    @Override public void onResults(SearchResults results, boolean isLoadingMore) {
        checkShouldHide(results.nbHits == 0);
        //TODO: Use results to implement text template
    }

    @Override public void onReset() {
    }

    @Override public void onError(Query query, AlgoliaException error) {
        checkShouldHide(true);
    }

    private void checkShouldHide(boolean newHideValue) {
        this.shouldHide = newHideValue;
        checkShouldHide();
    }

    private void checkShouldHide() {
        if (autoHide) {
            setVisibility(shouldHide ? GONE : VISIBLE);
        }
    }

    /**
     * Change the Toggle's attribute, updating facet refinements accordingly.
     *
     * @param newName the attribute's new name.
     */
    public void setAttributeName(String newName) {
        searcher.removeFacet(attributeName).addFacet(newName);
        if (isRefined) { // We need to update facetRefinement's attribute
            String valueRefined = isChecked() ? valueOn : valueOff;
            searcher.removeFacetRefinement(attributeName, valueRefined)
                    .addFacetRefinement(newName, valueRefined).search();
        }
        attributeName = newName;
    }

    /**
     * Change the Toggle's valueOn, updating facet refinements accordingly.
     *
     * @param newValue valueOn's new value.
     */
    public void setValueOn(String newValue) {
        if (isRefined && isChecked()) { // refining on valueOn: facetRefinement needs an update
            searcher.updateFacetRefinement(attributeName, valueOn, false)
                    .updateFacetRefinement(attributeName, newValue, true)
                    .search();
        }
        this.valueOn = newValue;
    }

    /**
     * Change the Toggle's valueOff, updating facet refinements accordingly.
     *
     * @param newValue valueOff's new value.
     */
    public void setValueOff(String newValue) {
        if (isRefined) { // we may need to update facets
            if (!isChecked()) { // refining on valueOff: facetRefinement needs an update
                searcher.updateFacetRefinement(attributeName, valueOff, false)
                        .updateFacetRefinement(attributeName, newValue, true)
                        .search();
            }
        } else { // now we have a valueOff, let's refine with it
            searcher.updateFacetRefinement(attributeName, newValue, true).search();
            isRefined = true;
        }
        this.valueOff = newValue;
    }

    public void setAutoHide(boolean autoHide) {
        this.autoHide = autoHide;
        checkShouldHide();
    }
}
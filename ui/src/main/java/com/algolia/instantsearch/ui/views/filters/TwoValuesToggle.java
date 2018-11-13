package com.algolia.instantsearch.ui.views.filters;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.CompoundButton;

import com.algolia.instantsearch.R;
import com.algolia.instantsearch.core.events.FacetRefinementEvent;
import com.algolia.instantsearch.core.model.Errors;
import com.algolia.instantsearch.core.model.SearchResults;

import org.greenrobot.eventbus.Subscribe;

import static com.algolia.instantsearch.core.events.RefinementEvent.Operation.ADD;
import static com.algolia.instantsearch.core.events.RefinementEvent.Operation.REMOVE;


public class TwoValuesToggle extends Toggle implements AlgoliaFilter {
    //TODO: Either propose a default good UX or document users should customize UX to show
    // this widget refines no matter if checked or not.

    /** The value to apply when the Toggle is checked. */
    private String valueOn;
    /** The value to apply when the Toggle is unchecked. */
    private String valueOff;

    /**
     * Constructs a new TwoValuesToggle with the given context's theme and the supplied attribute set.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     */
    public TwoValuesToggle(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TwoValuesToggle, 0, 0);
        try {
            valueOn = styledAttributes.getString(R.styleable.TwoValuesToggle_valueOn);
            valueOff = styledAttributes.getString(R.styleable.TwoValuesToggle_valueOff);
            if (valueOff == null) {
                throw new IllegalStateException(Errors.TOGGLE_MISSING_VALUEOFF);
            }
            if (valueOn == null) {
                throw new IllegalStateException(Errors.TOGGLE_MISSING_VALUEON);
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
        if (isChecked()) { // refining on valueOn: facetRefinement needs an update
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
            if (!isChecked()) { // refining on valueOff: facetRefinement needs an update
                searcher.updateFacetRefinement(attribute, valueOff, false)
                        .updateFacetRefinement(newName != null ? newName : attribute, newValue, true)
                        .search();
            }
        this.valueOff = newValue;
        applyEventualNewAttribute(newName);
    }

    @Override
    protected OnCheckedChangeListener getOnCheckedChangeListener() {
        searcher.updateFacetRefinement(attribute, valueOff, true);
        return new TwoValuesToggle.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // Toggle refined values
                    searcher.updateFacetRefinement(attribute, isChecked ? valueOn : valueOff, true)
                            .updateFacetRefinement(attribute, isChecked ? valueOff : valueOn, false)
                            .search();
            }
        };
    }

    @Override protected String applyTemplates(@NonNull SearchResults results) {
        return template
                .replace("{name}", attribute)
//FIXME                    .replace("{count}", String.valueOf(results.facets.get(attributeName).size()))
                .replace("{value}", String.valueOf(currentValue()));
    }

    private String currentValue() {
        return isChecked() ? valueOn : valueOff;
    }

    @Override protected void updateRefinementWithNewName(String newName) {
            String valueRefined = currentValue();
            searcher.removeFacetRefinement(attribute, valueRefined)
                    .addFacetRefinement(newName, valueRefined).search();
    }

    @Subscribe
    public void onFacetRefinementEvent(FacetRefinementEvent event) {
        if (event.attribute.equals(attribute)) {
            if (event.value.equals(valueOn)) {
                if (isChecked() && event.operation == REMOVE) { // Stop refining on valueOn -> toggle
                    setChecked(false);
                } else if (!isChecked() && event.operation == ADD) { // Start refining on valueOn -> toggle
                    setChecked(true);
                }
            } else if (event.value.equals(valueOff)) {
                if (!isChecked() && event.operation == REMOVE) { // Stop refining on valueOff -> toggle
                    setChecked(true);
                } else if (isChecked() && event.operation == ADD) { // Start refining on valueOff -> toggle
                    setChecked(false);
                }
            }
        }
    }
}

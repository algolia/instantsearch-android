package com.algolia.instantsearch.ui.views.filters;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;

import com.algolia.instantsearch.R;
import com.algolia.instantsearch.helpers.Searcher;
import com.algolia.instantsearch.model.SearchResults;
import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Query;

/**
 * A widget that toggles between refining and not refining an attribute with a given value.
 */
public abstract class Toggle extends AppCompatCheckBox implements AlgoliaFacetFilter {
    /** The attribute to refine on. */
    public String attributeName;
    /** Whether the OneValueToggle should hide when results are empty. */
    public boolean autoHide;
    /** A template to use as the OneValueToggle's text. */
    public String template;

    protected Searcher searcher;
    protected boolean shouldHide;
    private SearchResults lastResults;

    /**
     * Constructs a new Toggle with the given context's theme and the supplied attribute set.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     */
    public Toggle(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray filterStyledAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Filter, 0, 0);
        final TypedArray toggleStyledAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Toggle, 0, 0);
        try {
            attributeName = filterStyledAttributes.getString(R.styleable.Filter_attributeName);
            Filters.checkAttributeName(attributeName);
            template = toggleStyledAttributes.getString(R.styleable.Toggle_template);
            autoHide = filterStyledAttributes.getBoolean(R.styleable.Widget_autoHide, false);
        } finally {
            filterStyledAttributes.recycle();
            toggleStyledAttributes.recycle();
        }
    }

    /**
     * Changes the Toggle's attribute, updating facet refinements accordingly.
     *
     * @param newName the attribute's new name.
     */
    public final void setAttributeName(@NonNull String newName) {
        searcher.removeFacet(attributeName).addFacet(newName);
        updateRefinementWithNewName(newName);
        attributeName = newName;
    }

    /**
     * Changes the Toggle's autoHide setting, hiding it if needed.
     *
     * @param autoHide {@code true} if the Toggle should hide on empty results.
     */
    public final void setAutoHide(boolean autoHide) {
        this.autoHide = autoHide;
        Filters.hideIfShouldHide(this, autoHide, shouldHide);
    }

    public final void setTemplate(String template) {
        this.template = template;
        setText(applyTemplates(lastResults));
    }

    /** If given a new name, update searcher's facets and attribute. */
    protected void applyEventualNewName(@Nullable String newName) {
        if (newName != null) {
            searcher.removeFacet(attributeName).addFacet(newName);
            this.attributeName = newName;
        }
    }

    @NonNull @Override public final String getAttributeName() {
        return attributeName;
    }

    @Override public final void initWithSearcher(@NonNull final Searcher searcher) {
        this.searcher = searcher;

        // Setup user interaction listener
        setOnCheckedChangeListener(getOnCheckedChangeListener());

        // First Search to fill template, eventually applying valueOff refinement
        searcher.search();
    }

    @Override public final void onResults(@NonNull SearchResults results, boolean isLoadingMore) {
        shouldHide = results.nbHits == 0;
        Filters.hideIfShouldHide(this, autoHide, shouldHide);
        if (template != null) {
            setText(applyTemplates(results));
        }
        lastResults = results;
    }

    @Override public final void onError(Query query, AlgoliaException error) {
        Filters.hideIfShouldHide(this, autoHide, shouldHide);
    }

    /** Defines what happens when the checked state changes. */
    protected abstract OnCheckedChangeListener getOnCheckedChangeListener();

    /** Applies the text's templates according to the given results. */
    protected abstract String applyTemplates(@NonNull SearchResults results);

    /** Updates the refinements with the given new name. */
    protected abstract void updateRefinementWithNewName(String newName);
}
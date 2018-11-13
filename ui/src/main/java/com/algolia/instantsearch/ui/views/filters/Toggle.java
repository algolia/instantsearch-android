package com.algolia.instantsearch.ui.views.filters;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.algolia.instantsearch.R;
import com.algolia.instantsearch.core.helpers.Searcher;
import com.algolia.instantsearch.core.model.AlgoliaErrorListener;
import com.algolia.instantsearch.core.model.AlgoliaResultsListener;
import com.algolia.instantsearch.core.model.AlgoliaSearcherListener;
import com.algolia.instantsearch.core.model.SearchResults;
import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Query;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

/**
 * A widget that toggles between refining and not refining an attribute with a given value.
 */
public abstract class Toggle extends SwitchCompat implements AlgoliaFilter, AlgoliaResultsListener, AlgoliaErrorListener, AlgoliaSearcherListener {
    private final EventBus bus;
    /** The attribute to refine on. */
    public String attribute;
    /** Whether the OneValueToggle should hide when results are empty. */
    private boolean autoHide;
    /** A template to use as the OneValueToggle's text. */
    String template;

    Searcher searcher;
    private boolean shouldHide;
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
        final TypedArray viewStyledAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.View, 0, 0);
        final TypedArray widgetStyledAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Widget, 0, 0);
        final TypedArray toggleStyledAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Toggle, 0, 0);
        try {
            attribute = viewStyledAttributes.getString(R.styleable.View_attribute);
            Filters.checkAttributeName(attribute);
            template = toggleStyledAttributes.getString(R.styleable.Toggle_template);
            autoHide = widgetStyledAttributes.getBoolean(R.styleable.Widget_autoHide, false);
        } finally {
            widgetStyledAttributes.recycle();
            toggleStyledAttributes.recycle();
            viewStyledAttributes.recycle();
        }
        bus = EventBus.getDefault();
        bus.register(this);
    }

    /**
     * Changes the Toggle's attribute, updating facet refinements accordingly.
     *
     * @param newAttribute the attribute's new name.
     */
    public final void setAttribute(@NonNull String newAttribute) {
        searcher.removeFacet(attribute).addFacet(newAttribute);
        updateRefinementWithNewName(newAttribute);
        attribute = newAttribute;
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

    /** If given a new attribute, update searcher's facets and attribute. */
    void applyEventualNewAttribute(@Nullable String newAttribute) {
        if (newAttribute != null) {
            searcher.removeFacet(attribute).addFacet(newAttribute);
            this.attribute = newAttribute;
        }
    }

    @NonNull public final String getAttribute() {
        return attribute;
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

    @Override public final void onError(@NonNull Query query, @NonNull AlgoliaException error) {
        Filters.hideIfShouldHide(this, autoHide, shouldHide);
    }

    /**
     * Defines what happens when the checked state changes.
     * A Toggle subclass can use this method to pass its initial state to the Searcher.
     * @return a listener that will be called when the Toggle is checked.
     */
    protected abstract OnCheckedChangeListener getOnCheckedChangeListener();

    /** Applies the text's templates according to the given results.
     *
     * @param results the current SearchResults.
     * @return the templated string after replacing the placeholders using {@code results}.
     */
    protected abstract String applyTemplates(@NonNull SearchResults results);

    /** Updates the refinements with the given new name.
     * @param newName the new attribute name.
     */
    protected abstract void updateRefinementWithNewName(String newName);
}

package com.algolia.instantsearch.ui.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.algolia.instantsearch.R;
import com.algolia.instantsearch.events.FacetRefinementEvent;
import com.algolia.instantsearch.events.ResetEvent;
import com.algolia.instantsearch.helpers.Searcher;
import com.algolia.instantsearch.model.AlgoliaResultsListener;
import com.algolia.instantsearch.model.AlgoliaSearcherListener;
import com.algolia.instantsearch.model.Errors;
import com.algolia.instantsearch.model.FacetValue;
import com.algolia.instantsearch.model.SearchResults;
import com.algolia.instantsearch.ui.views.filters.AlgoliaFilter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static com.algolia.instantsearch.events.RefinementEvent.Operation.ADD;
import static com.algolia.instantsearch.events.RefinementEvent.Operation.REMOVE;

/**
 * Displays facet values for an attribute and lets the user filter the results using these values.
 */
public class RefinementList extends ListView implements AlgoliaFilter, AlgoliaResultsListener, AlgoliaSearcherListener {
    /** The operation for disjunctive faceting (foo OR bar). */
    public static final int OPERATION_OR = 0;
    /** The operation for disjunctive faceting (foo AND bar). */
    public static final int OPERATION_AND = 1;

    /** The value for sorting by increasing counts. */
    public static final String SORT_COUNT_ASC = "count:asc";
    /** The value for sorting by decreasing counts. */
    public static final String SORT_COUNT_DESC = "count:desc";
    /** The value for sorting by refined first. */
    public static final String SORT_ISREFINED = "isRefined";
    /** The value for sorting by name in alphabetical order. */
    public static final String SORT_NAME_ASC = "name:asc";
    /** The value for sorting by name in reverse alphabetical order. */
    public static final String SORT_NAME_DESC = "name:desc";

    /** The default {@link RefinementList#SORT_COUNT_DESC sorting method}. */
    public static final List<String> DEFAULT_SORT = Arrays.asList(SORT_ISREFINED, SORT_COUNT_DESC);
    /** The default maximum amount of values to display. */
    public static final int DEFAULT_LIMIT = 10;

    @NonNull
    private final String attribute;
    private final int operation;
    /** The current sort order for displaying values. */
    @NonNull
    private final List<String> sortOrder;
    /** The current maximum amount of values to display. */
    private int limit;

    @NonNull
    private final FacetAdapter adapter;
    private Searcher searcher;
    @NonNull
    private Comparator<? super FacetValue> sortComparator;

    /**
     * Constructs a new RefinementList with the given context's theme and the supplied attribute set.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     */
    @SuppressWarnings("ConstantConditions") /* We set to null only if isInEditMode and throw if attribute is null */
    public RefinementList(@NonNull final Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            operation = OPERATION_AND;
            sortOrder = null;
            sortComparator = null;
            attribute = null;
            adapter = null;
            return;
        }

        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RefinementList, 0, 0);
        final TypedArray viewAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.View, 0, 0);
        try {
            attribute = viewAttributes.getString(R.styleable.View_attribute);
            if (attribute == null) {
                throw new IllegalStateException(Errors.REFINEMENTS_MISSING_ATTRIBUTE);
            }

            operation = styledAttributes.getInt(R.styleable.RefinementList_operation, OPERATION_OR);
            limit = styledAttributes.getInt(R.styleable.RefinementList_limit, DEFAULT_LIMIT);

            ArrayList<String> parsedSortOrder = parseSortOrder(styledAttributes.getString(R.styleable.RefinementList_sortBy));
            sortOrder = parsedSortOrder != null ? parsedSortOrder : DEFAULT_SORT;
        } finally {
            styledAttributes.recycle();
            viewAttributes.recycle();
        }

        adapter = new FacetAdapter(context);
        setAdapter(adapter);

        sortComparator = new Comparator<FacetValue>() {
            @Override
            public int compare(@NonNull FacetValue lhs, @NonNull FacetValue rhs) {
                // For each value of sortOrder, try to sort with it and continue if tie (value==0)
                int comparisonValue = 0;
                for (String sortValue : sortOrder) {
                    switch (sortValue) {
                        case SORT_COUNT_ASC:
                            comparisonValue = Integer.valueOf(lhs.count).compareTo(rhs.count);
                            break;
                        case SORT_COUNT_DESC:
                            comparisonValue = Integer.valueOf(rhs.count).compareTo(lhs.count);
                            break;
                        case SORT_ISREFINED:
                            comparisonValue = -Boolean.valueOf(adapter.hasActive(lhs.value)).compareTo(adapter.hasActive(rhs.value));
                            break;
                        case SORT_NAME_ASC:
                            comparisonValue = lhs.value.compareTo(rhs.value);
                            break;
                        case SORT_NAME_DESC:
                            comparisonValue = rhs.value.compareTo(lhs.value);
                            break;
                    }
                    if (comparisonValue != 0) {
                        break;
                    }
                }

                return comparisonValue;
            }
        };
    }

    @Override
    public void initWithSearcher(@NonNull Searcher searcher) {
        this.searcher = searcher;
    }

    @Override
    public void onResults(@NonNull SearchResults results, boolean isLoadingMore) {
        if (isLoadingMore) { // more results of the same request -> facetValues should not change
            return;
        }

        if (results.nbHits == 0) { // no results for query, facet counts should be set to 0
            adapter.resetFacetCounts();
            return;
        }

        // else build updated facet list
        final Map<String, List<FacetValue>> facets = results.facets;
        List<FacetValue> refinementFacets = facets.get(attribute);

        // If we have new facetValues we should use them, and else set count=0 to old ones
        if (refinementFacets != null && refinementFacets.size() > 0) {
            adapter.clear(false);
            adapter.addAll(refinementFacets);
            adapter.sort(sortComparator);
        } else {
            adapter.resetFacetCounts();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetachedFromWindow() {
        EventBus.getDefault().unregister(this);
        super.onDetachedFromWindow();
    }

    @Subscribe
    public void onReset(ResetEvent event) {
        adapter.clear();
    }

    @Subscribe
    public void onRefinement(FacetRefinementEvent event) {
        if (event.attribute.equals(attribute)) {
            if (adapter.hasActive(event.value) && event.operation.equals(REMOVE) || // if (active != ADD)
                    !adapter.hasActive(event.value) && event.operation.equals(ADD)) {
                adapter.toggleFacetValue(event.value);
            }
        }
    }

    /**
     * Replaces the default sortComparator by a custom one respecting the {@link Comparator} interface.
     *
     * @param sortComparator a new Comparator to use for sorting facetValues.
     * @see #DEFAULT_SORT
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public void setSortComparator(@NonNull Comparator<? super FacetValue> sortComparator) {
        this.sortComparator = sortComparator;
    }

    /**
     * Gets the attribute that this RefinementList refines on.
     *
     * @return the RefinementList's {@link RefinementList#attribute}.
     */
    @Override
    public @NonNull String getAttribute() {
        return attribute;
    }

    /**
     * Gets the operation used by this RefinementList for filtering.
     *
     * @return the RefinementList's {@link RefinementList#operation}.
     * @see #OPERATION_AND
     * @see #OPERATION_OR
     */
    public int getOperation() {
        return operation;
    }

    @Nullable
    static ArrayList<String> parseSortOrder(@Nullable String attribute) {
        if (attribute == null) {
            return null;
        }

        ArrayList<String> sortOrder = new ArrayList<>();

        switch (attribute) {
            case SORT_COUNT_ASC:
            case SORT_COUNT_DESC:
            case SORT_ISREFINED:
            case SORT_NAME_ASC:
            case SORT_NAME_DESC:
                sortOrder.add(attribute);
                break;
            default:
                if (!attribute.startsWith("[")) {
                    throw new IllegalStateException(String.format(Errors.SORT_INVALID_VALUE, attribute));
                }
                JSONArray array;
                try {
                    array = new JSONArray(attribute);
                    for (int i = 0; i < array.length(); i++) {
                        String value = array.optString(i);
                        addSortOrderOrThrow(value, sortOrder);
                    }
                } catch (JSONException e) {
                    throw new IllegalStateException(String.format(Errors.SORT_INVALID_ARRAY, attribute));
                }
        }

        return sortOrder;
    }

    private static void addSortOrderOrThrow(@NonNull String value, @NonNull ArrayList<String> sortOrder) {
        switch (value) {
            case SORT_COUNT_ASC:
            case SORT_COUNT_DESC:
            case SORT_ISREFINED:
            case SORT_NAME_ASC:
            case SORT_NAME_DESC:
                sortOrder.add(value);
                break;
            default:
                throw new IllegalStateException(String.format(Errors.SORT_INVALID_VALUE, value));
        }
    }

    private class FacetAdapter extends ArrayAdapter<FacetValue> {
        @NonNull
        private final List<FacetValue> facetValues;

        private final HashSet<String> activeFacets = new HashSet<>();

        FacetAdapter(Context context) {
            this(context, new ArrayList<FacetValue>());
        }

        private FacetAdapter(Context context, @NonNull List<FacetValue> facetValues) {
            super(context, -1, facetValues);
            this.facetValues = new ArrayList<>(facetValues);
        }

        void clear(boolean clearActiveFacets) {
            super.clear();
            if (clearActiveFacets) {
                activeFacets.clear();
            }
            facetValues.clear();
        }

        @Override
        public void clear() {
            clear(true);
        }

        @Override
        public void add(FacetValue facetValue) {
            addFacet(facetValue);
        }

        @Override
        public void addAll(@NonNull Collection<? extends FacetValue> items) {
            for (FacetValue facetValue : items) {
                addFacet(facetValue);
            }
        }

        @Override
        public void remove(@Nullable FacetValue facet) {
            super.remove(facet);

            facetValues.remove(facet);
            if (facet != null) {
                activeFacets.remove(facet.value);
            }
        }

        @Override
        public void sort(@NonNull Comparator<? super FacetValue> comparator) {
            super.sort(comparator);
            Collections.sort(facetValues, comparator);
        }

        @SuppressLint("DefaultLocale") // Formats are for exception messages
        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
            if (convertView == null) {
                convertView = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.refinement_row, parent, false);
            }

            final FacetValue facet = getItem(position);
            if (facet == null) {
                throw new IllegalStateException(String.format(Errors.REFINEMENTS_MISSING_ITEM, position));
            }

            final TextView nameView = (TextView) convertView.findViewById(R.id.refinementName);
            final TextView countView = (TextView) convertView.findViewById(R.id.refinementCount);
            nameView.setText(facet.value);
            countView.setText(String.valueOf(facet.count));
            updateFacetViews(facet, nameView, countView);
            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    final FacetValue facet = adapter.getItem(position);
                    if (facet == null) {
                        throw new IllegalStateException(String.format(Errors.REFINEMENTS_MISSING_ITEM, position));
                    }

                    toggleFacetValue(facet.value);
                    sort(sortComparator);
                    searcher.updateFacetRefinement(attribute, facet.value, hasActive(facet.value)).search();
                    updateFacetViews(facet, nameView, countView);
                }
            });
            return convertView;
        }

        private void toggleFacetValue(String facetValue) {
            final boolean wasActive = hasActive(facetValue);
            if (wasActive) {
                activeFacets.remove(facetValue);
            } else {
                activeFacets.add(facetValue);
            }
        }

        private boolean hasActive(String facetName) {
            return activeFacets.contains(facetName);
        }

        void addFacet(FacetValue facetValue) {
            facetValues.add(facetValue);

            // Add to visible facetValues if we didn't reach the limit
            if (getCount() < limit) {
                super.add(facetValue);
            }
        }

        private void updateFacetViews(@NonNull FacetValue facet, @NonNull TextView nameView, @NonNull TextView countView) {
            if (hasActive(facet.value)) {
                nameView.setPaintFlags(nameView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                countView.setTypeface(null, Typeface.BOLD);
            } else {
                nameView.setPaintFlags(nameView.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                countView.setTypeface(null, Typeface.NORMAL);
            }
        }

        private void resetFacetCounts() {
            for (FacetValue facetValue : facetValues) {
                facetValue.count = 0;
            }
        }
    }


}

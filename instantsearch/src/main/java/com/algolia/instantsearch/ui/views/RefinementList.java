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
import com.algolia.instantsearch.helpers.Searcher;
import com.algolia.instantsearch.model.Errors;
import com.algolia.instantsearch.model.FacetValue;
import com.algolia.instantsearch.model.SearchResults;
import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Query;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class RefinementList extends ListView implements AlgoliaWidget {
    public static final int OPERATOR_OR = 0;
    public static final int OPERATOR_AND = 1;

    public static final String SORT_COUNT_ASC = "count:asc";
    public static final String SORT_COUNT_DESC = "count:desc";
    public static final String SORT_ISREFINED = "isRefined";
    public static final String SORT_NAME_ASC = "name:asc";
    public static final String SORT_NAME_DESC = "name:desc";

    public static final String DEFAULT_SORT = SORT_COUNT_DESC;
    public static final int DEFAULT_LIMIT = 10;

    @NonNull
    private final String attribute;
    private final int operator;
    @NonNull
    private final ArrayList<String> sortOrder;
    private int limit;

    @NonNull
    private final FacetAdapter adapter;
    private Searcher searcher;
    @NonNull
    private Comparator<? super FacetValue> sortComparator;

    @SuppressWarnings("ConstantConditions") /* We set to null only if isInEditMode and throw if attribute is null */
    public RefinementList(@NonNull final Context context, AttributeSet attrs) throws AlgoliaException {
        super(context, attrs);
        if (isInEditMode()) {
            operator = OPERATOR_AND;
            sortOrder = null;
            sortComparator = null;
            attribute = null;
            adapter = null;
            return;
        }

        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RefinementList, 0, 0);
        try {
            attribute = styledAttributes.getString(R.styleable.View_attribute);
            if (attribute == null) {
                throw new AlgoliaException(Errors.REFINEMENTS_MISSING_ATTRIBUTE);
            }

            operator = styledAttributes.getInt(R.styleable.RefinementList_operator, OPERATOR_OR);
            limit = styledAttributes.getInt(R.styleable.RefinementList_limit, DEFAULT_LIMIT);

            ArrayList<String> parsedSortOrder = parseSortOrder(styledAttributes.getString(R.styleable.RefinementList_sortBy));
            sortOrder = parsedSortOrder != null ? parsedSortOrder : new ArrayList<>(Collections.singletonList(DEFAULT_SORT));
        } finally {
            styledAttributes.recycle();
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
    public void onError(Query query, @NonNull AlgoliaException error) {
    }

    @Override
    public void onReset() {
        adapter.clear();
    }

    /**
     * Replace the default sortComparator by a custom one respecting the {@link Comparator} interface.
     *
     * @param sortComparator a new Comparator to use for sorting facetValues.
     */
    public void setSortComparator(@NonNull Comparator<? super FacetValue> sortComparator) {
        this.sortComparator = sortComparator;
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

    @NonNull
    public String getAttribute() {
        return attribute;
    }

    public int getOperator() {
        return operator;
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

        public void clear(boolean clearActiveFacets) {
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

                    final boolean wasActive = hasActive(facet.value);
                    if (wasActive) {
                        activeFacets.remove(facet.value);
                    } else {
                        activeFacets.add(facet.value);
                    }
                    sort(sortComparator);
                    searcher.updateFacetRefinement(attribute, facet.value, hasActive(facet.value)).search();
                    updateFacetViews(facet, nameView, countView);
                }
            });
            return convertView;
        }

        private boolean hasActive(String facetName) {
            return activeFacets.contains(facetName);
        }

        public void addFacet(FacetValue facetValue) {
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

package com.algolia.instantsearch.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.algolia.instantsearch.AlgoliaHelper;
import com.algolia.instantsearch.R;
import com.algolia.instantsearch.model.Errors;
import com.algolia.instantsearch.model.Facet;
import com.algolia.search.saas.AlgoliaException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class RefinementList extends ListView {
    public static final int OPERATOR_OR = 0;
    public static final int OPERATOR_AND = 1;

    public static final String SORT_COUNT = "count";
    public static final String SORT_ISREFINED = "isRefined";
    public static final String SORT_NAME_ASC = "name:asc";
    public static final String SORT_NAME_DESC = "name:desc";

    public static final String DEFAULT_SORT = SORT_COUNT;
    public static final int DEFAULT_LIMIT = 10;

    private final String attributeName;
    private final int operator;
    private final ArrayList<String> sortOrder;
    private int limit;

    private final FacetAdapter adapter;
    private AlgoliaHelper helper;
    private Comparator<? super Facet> sortComparator;

    public RefinementList(final Context context, AttributeSet attrs) throws AlgoliaException {
        super(context, attrs);

        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RefinementList, 0, 0);
        try {
            attributeName = styledAttributes.getString(R.styleable.RefinementList_attribute);
            if (attributeName == null) {
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

        sortComparator = new Comparator<Facet>() {
            @Override
            public int compare(Facet lhs, Facet rhs) {
                // For each value of sortOrder, try to sort with it and continue if tie (value==0)
                int comparisonValue = 0;
                for (String sortValue : sortOrder) {
                    switch (sortValue) {
                        case SORT_COUNT:
                            comparisonValue = -Integer.valueOf(lhs.getCount()).compareTo(rhs.getCount());
                            break;
                        case SORT_ISREFINED:
                            comparisonValue = -Boolean.valueOf(lhs.isEnabled()).compareTo(rhs.isEnabled());
                            break;
                        case SORT_NAME_ASC:
                            comparisonValue = lhs.getName().compareTo(rhs.getName());
                            break;
                        case SORT_NAME_DESC:
                            comparisonValue = rhs.getName().compareTo(lhs.getName());
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

    public void onInit(AlgoliaHelper helper) {
        this.helper = helper;
        helper.registerRefinementList(this);
    }

    public void onUpdateView(JSONObject content, boolean isLoadingMore) {
        if (isLoadingMore) { // more results of the same request -> facets should not change
            return;
        }

        if (content == null) { // no results for query, facet counts should be set to 0
            adapter.resetFacetCounts();
            return;
        }

        // else build updated facet list
        ArrayList<Facet> newFacets = new ArrayList<>();

        JSONObject resultFacets = content.optJSONObject("facets");
        if (resultFacets != null) {
            JSONObject refinementFacets = resultFacets.optJSONObject(attributeName);
            if (refinementFacets != null) {
                final Iterator<String> iterKeys = refinementFacets.keys();
                while (iterKeys.hasNext()) {
                    final String key = iterKeys.next();
                    int value = refinementFacets.optInt(key);
                    final boolean wasActive = adapter.hasActive(key);
                    final Facet facet = new Facet(key, value, wasActive);
                    newFacets.add(facet);
                }
            }

            // If we have new facets we should use them, and else set count=0 to old ones
            if (newFacets.size() > 0) {
                adapter.clear();
                adapter.addAll(newFacets);
                adapter.sort(sortComparator);
            } else {
                adapter.resetFacetCounts();
            }
            adapter.notifyDataSetChanged();
        }
    }

    public void reset() {
        adapter.clear();
    }

    protected static ArrayList<String> parseSortOrder(String string) {
        if (string == null) {
            return null;
        }

        ArrayList<String> sortOrder = new ArrayList<>();

        JSONArray array;
        try {
            array = new JSONArray(string);
            for (int i = 0; i < array.length(); i++) {
                String value = array.optString(i);
                addSortOrderOrThrow(value, sortOrder);
            }
        } catch (JSONException e) {
            // The attribute was not a valid JSONArray. Maybe it was a single valid sortOrder?
            try {
                addSortOrderOrThrow(string, sortOrder);
            } catch (IllegalStateException e2) { // The string was neither a single sortOrder
                throw new IllegalStateException(String.format(Errors.SORT_INVALID_VALUE, string));
            }
        }

        return sortOrder;
    }

    private static void addSortOrderOrThrow(String string, ArrayList<String> sortOrder) {
        switch (string) {
            case SORT_COUNT:
            case SORT_ISREFINED:
            case SORT_NAME_ASC:
            case SORT_NAME_DESC:
                sortOrder.add(string);
                break;
            default:
                throw new IllegalStateException(String.format(Errors.SORT_INVALID_VALUE, string));
        }
    }

    public String getAttributeName() {
        return attributeName;
    }

    public int getOperator() {
        return operator;
    }

    private class FacetAdapter extends ArrayAdapter<Facet> {
        private List<Facet> facets;

        private HashSet<String> activeFacets = new HashSet<>();

        public FacetAdapter(Context context) {
            this(context, new ArrayList<Facet>());
        }

        private FacetAdapter(Context context, List<Facet> facets) {
            super(context, -1, facets);
            this.facets = new ArrayList<>(facets);
        }

        @Override
        public void clear() {
            super.clear();
            activeFacets.clear();
            facets.clear();
        }

        @Override
        public void add(Facet facet) {
            addFacet(facet);
        }

        @Override
        public void addAll(Collection<? extends Facet> items) {
            for (Facet facet : items) {
                addFacet(facet);
            }
        }

        @Override
        public void remove(Facet facet) {
            super.remove(facet);

            facets.remove(facet);
            activeFacets.remove(facet.getName());
        }

        @Override
        public void sort(Comparator<? super Facet> comparator) {
            super.sort(comparator);
            Collections.sort(facets, comparator);
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (convertView == null) {
                convertView = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.refinement_row, parent, false);
            }

            final Facet facet = getItem(position);

            final TextView nameView = (TextView) convertView.findViewById(R.id.refinementName);
            final TextView countView = (TextView) convertView.findViewById(R.id.refinementCount);
            nameView.setText(facet.getName());
            countView.setText(String.valueOf(facet.getCount()));
            updateFacetTextView(facet, nameView);
            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Facet facet = adapter.getItem(position);

                    facet.setEnabled(!facet.isEnabled());
                    updateActiveStatus(facet);

                    helper.updateFacetRefinement(attributeName, facet);
                    updateFacetTextView(facet, nameView);
                }
            });
            return convertView;
        }

        private boolean hasActive(String facetName) {
            return activeFacets.contains(facetName);
        }

        private void updateActiveStatus(Facet facet) {
            if (facet.isEnabled()) {
                activeFacets.add(facet.getName());
            } else {
                activeFacets.remove(facet.getName());
            }
            sort(sortComparator);
        }


        public void addFacet(Facet facet) {
            facets.add(facet);

            // Add to visible facets if we didn't reach the limit
            if (getCount() < limit) {
                super.add(facet);
            }

            if (facet.isEnabled()) {
                activeFacets.add(facet.getName());
            }
        }

        private void updateFacetTextView(Facet facet, TextView viewName) {
            if (facet.isEnabled()) {
                viewName.setPaintFlags(viewName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            } else {
                viewName.setPaintFlags(viewName.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
            }
        }

        private void resetFacetCounts() {
            for (Facet facet : facets) {
                facet.setCount(0);
            }
        }
    }


}

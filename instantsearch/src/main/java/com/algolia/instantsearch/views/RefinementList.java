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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class RefinementList extends ListView {
    public static final int OPERATOR_OR = 0;
    public static final int OPERATOR_AND = 1;
    public static final int DEFAULT_LIMIT = 10;

    private final String attributeName;
    private final int operator;
    private int limit;
    private final boolean autoHide;

    private final FacetAdapter adapter;
    private AlgoliaHelper helper;

    public RefinementList(final Context context, AttributeSet attrs) throws AlgoliaException {
        super(context, attrs);

        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RefinementList, 0, 0);
        try {
            attributeName = styledAttributes.getString(R.styleable.RefinementList_attribute);
            if (attributeName == null) {
                throw new AlgoliaException(Errors.REFINEMENTS_MISSING_ATTRIBUTE);
            }

            operator = styledAttributes.getInt(R.styleable.RefinementList_operator, OPERATOR_OR);
            limit = styledAttributes.getInt(R.styleable.RefinementList_limit, DEFAULT_LIMIT);//TODO: Use!
            autoHide = styledAttributes.getBoolean(R.styleable.RefinementList_autoHide, false);

            final String sortByString = styledAttributes.getString(R.styleable.RefinementList_sortBy);//TODO: Parse!
        } finally {
            styledAttributes.recycle();
        }

        adapter = new FacetAdapter(context);
        adapter.loadFakeData();
        setAdapter(adapter);
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void onUpdateView(JSONObject content, boolean isLoadingMore) {
        if (isLoadingMore || content == null) {
            // either we did load more results of the same request, or the last request
            // did return no results. In both cases, facets should not change.
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
                    final boolean wasThere = adapter.contains(key);
                    newFacets.add(new Facet(key, value, wasThere));
                }
            }

            adapter.clear();
            adapter.addAll(newFacets);
            adapter.notifyDataSetChanged();
        }
    }

    public void reset() {
        adapter.clear();
    }


    private class FacetAdapter extends ArrayAdapter<Facet> {
        private HashSet<String> activeFacets = new HashSet<>();

        public FacetAdapter(Context context) {
            this(context, new ArrayList<Facet>());
        }

        private FacetAdapter(Context context, List<Facet> facets) {
            super(context, -1, facets);
        }

        private boolean contains(String facetName) {
            return activeFacets.contains(facetName);
        }

        private void updateActiveStatus(Facet facet) {
            if (facet.isEnabled()) {
                activeFacets.add(facet.getName());
            } else {
                activeFacets.remove(facet.getName());
            }
        }

        @Override
        public void clear() {
            super.clear();
            activeFacets.clear();
        }

        @Override
        public void add(Facet facet) {
            super.add(facet);
            if (facet.isEnabled()) {
                activeFacets.add(facet.getName());
            }
        }

        @Override
        public void addAll(Collection<? extends Facet> items) {
            super.addAll(items);
            for (Facet facet : items) {
                if (facet.isEnabled()) {
                    activeFacets.add(facet.getName());
                }
            }
        }

        @Override
        public void remove(Facet facet) {
            super.remove(facet);
            activeFacets.remove(facet.getName());
        }

        public void loadFakeData() {
            clear();
            add(new Facet("Paris", 42));
            add(new Facet("SF", 21));
            add(new Facet("Tours", 12));
            add(new Facet("Bordeaux", 8));
            notifyDataSetChanged();
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

        private void updateFacetTextView(Facet facet, TextView viewName) {
            if (facet.isEnabled()) {
                viewName.setPaintFlags(viewName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            } else {
                viewName.setPaintFlags(viewName.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
            }
        }
    }

    public void onInit(AlgoliaHelper helper) {
        this.helper = helper;
    }


}

package com.algolia.instantsearch.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.algolia.instantsearch.R;
import com.algolia.instantsearch.model.Errors;
import com.algolia.search.saas.AlgoliaException;

import org.json.JSONObject;

import java.util.ArrayList;
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

    public RefinementList(Context context, AttributeSet attrs) throws AlgoliaException {
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
        if (isLoadingMore) { // do nothing, loading next page does not change facets
            return;
        }

        adapter.clear(); // in every other case, delete current facets
        if (content == null) {
            return; // and return if we have no results anymore
        }

        // else build updated facet list
        JSONObject resultFacets = content.optJSONObject("facets");
        if (resultFacets != null) {
            JSONObject refinementFacets = resultFacets.optJSONObject(attributeName);
            if (refinementFacets != null) {
                final Iterator<String> iterKeys = refinementFacets.keys();
                while (iterKeys.hasNext()) {
                    final String key = iterKeys.next();
                    int value = refinementFacets.optInt(key);
                    adapter.add(new Facet(key, value));
                }
            }

            adapter.notifyDataSetChanged();
        }
    }


    private class FacetAdapter extends ArrayAdapter<Facet> {
        public FacetAdapter(Context context) {
            this(context, new ArrayList<Facet>());
        }

        private FacetAdapter(Context context, List<Facet> givenFacets) {
            super(context, -1, givenFacets);
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
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.refinement_row, parent, false);
            }

            Facet facet = getItem(position);

            final TextView nameView = (TextView) convertView.findViewById(R.id.refinementName);
            final TextView countView = (TextView) convertView.findViewById(R.id.refinementCount);
            nameView.setText(facet.name);
            countView.setText(String.valueOf(facet.count));

            return convertView;
        }

    }


    private class Facet {
        String name;
        int count;
        boolean isEnabled;

        public Facet(String name, int count, boolean isEnabled) {
            this.name = name;
            this.count = count;
            this.isEnabled = isEnabled;
        }

        public Facet(String name, int count) {
            this.name = name;
            this.count = count;
        }

        public boolean isEnabled() {
            return isEnabled;
        }
    }
}

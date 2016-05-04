package com.algolia.instantsearch.helper;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.algolia.instantsearch.R;
import com.algolia.search.saas.AlgoliaException;

import java.util.Collection;

public class Hits extends ListView {

    private final Integer hitsPerPage;

    private final String[] attributesToRetrieve;
    private final String[] attributesToHighlight;

    private ArrayAdapter<String> adapter;

    public Hits(Context context, AttributeSet attrs) throws AlgoliaException {
        super(context, attrs);
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Hits, 0, 0);
        initAdapter();
        try {
            hitsPerPage = styledAttributes.getInt(R.styleable.Hits_hitsPerPage, AlgoliaHelper.DEFAULT_HITS_PER_PAGE);
            attributesToRetrieve = getAttributes(styledAttributes, R.styleable.Hits_attributesToRetrieve);
            attributesToHighlight = getAttributes(styledAttributes, R.styleable.Hits_attributesToHighlight);
        } finally {
            styledAttributes.recycle();
        }
    }

    private void initAdapter() {
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        setAdapter(adapter);
    }

    public void clear() {
        adapter.clear();
        adapter.notifyDataSetChanged();
    }

    public void add(Collection<String> results) {
        adapter.addAll(results);
        adapter.notifyDataSetChanged();
    }

    public void replace(Collection<String> values, boolean scrollToTop) {
        adapter.clear();
        add(values);
        if (scrollToTop) {
            smoothScrollToPosition(0);
        }
    }

    public void replace(Collection<String> values) {
        replace(values, true);
    }

    private String[] getAttributes(TypedArray styledAttributes, int attributeResourceId) {
        String attributesToRetrieveStr = styledAttributes.getString(attributeResourceId);
        if (attributesToRetrieveStr == null) {
            attributesToRetrieveStr = AlgoliaHelper.DEFAULT_ATTRIBUTES;
        }
        return attributesToRetrieveStr.split(",");
    }

    public Integer getHitsPerPage() {
        return hitsPerPage;
    }

    public String[] getAttributesToRetrieve() {
        return attributesToRetrieve;
    }

    public String[] getAttributesToHighlight() {
        return attributesToHighlight;
    }
}
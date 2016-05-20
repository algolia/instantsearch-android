package com.algolia.instantsearch.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.algolia.instantsearch.AlgoliaHelper;
import com.algolia.instantsearch.R;
import com.algolia.instantsearch.model.Result;
import com.algolia.instantsearch.ui.ResultsAdapter;
import com.algolia.search.saas.AlgoliaException;

import java.util.Collection;

public class Hits extends RecyclerView {

    private final Integer hitsPerPage;
    private final String[] attributesToRetrieve;
    private final String[] attributesToHighlight;
    private final String layoutName;

    private ResultsAdapter adapter;
    private LayoutManager layoutManager;
    private View emptyView;

    public Hits(Context context, AttributeSet attrs) throws AlgoliaException {
        super(context, attrs);
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Hits, 0, 0);
        try {
            hitsPerPage = styledAttributes.getInt(R.styleable.Hits_hitsPerPage, AlgoliaHelper.DEFAULT_HITS_PER_PAGE);
            attributesToRetrieve = getAttributes(styledAttributes, R.styleable.Hits_attributesToRetrieve);
            attributesToHighlight = getAttributes(styledAttributes, R.styleable.Hits_attributesToHighlight);
            layoutName = styledAttributes.getString(R.styleable.Hits_itemLayout);
        } finally {
            styledAttributes.recycle();
        }
        adapter = new ResultsAdapter();
        adapter.registerAdapterDataObserver(new AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                updateEmptyView();
            }
        });
        setAdapter(adapter);

        layoutManager = new LinearLayoutManager(context);
        setLayoutManager(layoutManager);
    }

    public void clear() {
        adapter.clear();
        adapter.notifyDataSetChanged();
    }

    public void add(Collection<Result> results) {
        int currentCount = adapter.getItemCount();
        for (Result res : results) {
            adapter.add(res);
        }
        adapter.notifyItemRangeInserted(currentCount, results.size());
    }

    public void replace(Collection<Result> values, boolean scrollToTop) {
        adapter.clear();
        add(values);
        if (scrollToTop) {
            smoothScrollToPosition(0);
        }
    }

    public void replace(Collection<Result> values) {
        replace(values, true);
    }

    private String[] getAttributes(TypedArray styledAttributes, int attributeResourceId) {
        String attributeString = styledAttributes.getString(attributeResourceId);
        if (attributeString == null) {
            attributeString = AlgoliaHelper.DEFAULT_ATTRIBUTES;
        }
        final String[] splitAttributes = attributeString.split(",");
        final String[] cleanAttributes = new String[splitAttributes.length];

        for (int i = 0; i < splitAttributes.length; i++) {
            cleanAttributes[i] = splitAttributes[i].trim();
        }
        return cleanAttributes;
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

    private void updateEmptyView() {
        if (adapter.getItemCount() == 0) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
        }
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

    public String getLayoutName() {
        return layoutName;
    }
}
package com.algolia.instantsearch.helper;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.algolia.instantsearch.R;
import com.algolia.instantsearch.helper.databinding.Result;
import com.algolia.instantsearch.helper.databinding.ResultsAdapter;
import com.algolia.search.saas.AlgoliaException;

import java.util.Collection;

public class Hits extends RecyclerView {

    private final Integer hitsPerPage;
    private final String[] attributesToRetrieve;
    private final String[] attributesToHighlight;
    private final String attributeToDisplay;
    private final int itemLayout;

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
            attributeToDisplay = styledAttributes.getString(R.styleable.Hits_attributeToDisplay);
            String layoutIdStr = styledAttributes.getString(R.styleable.Hits_itemLayout);
            if (layoutIdStr == null) {
                layoutIdStr = "";
            } else {
                layoutIdStr = layoutIdStr.replace("android.", "").replace("R.layout.", "");
            }
            itemLayout = getResources().getIdentifier(layoutIdStr, "layout", context.getPackageName());
            if (itemLayout == 0) {
                throw new RuntimeException(layoutIdStr + " is not a valid layout identifier.");
            }

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

    public void add(Collection<String> results) {
        for (String res: results) {
            adapter.add(new Result(res));
        }
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

    public String getAttributeToDisplay() {
        return attributeToDisplay;
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
}
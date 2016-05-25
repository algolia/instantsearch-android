package com.algolia.instantsearch.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import com.algolia.instantsearch.AlgoliaHelper;
import com.algolia.instantsearch.R;
import com.algolia.instantsearch.model.Result;
import com.algolia.instantsearch.ui.ResultsAdapter;
import com.algolia.search.saas.AlgoliaException;

import java.util.Collection;

public class Hits extends RecyclerView implements AlgoliaResultsView {
    public static final int MISSING_VALUE = -42;
    public static final int DEFAULT_HITS_PER_PAGE = 20;
    public static final int DEFAULT_REMAINING_ITEMS = 5;

    private final Integer hitsPerPage;
    private final int remainingItemsBeforeLoading; // Minimum number of remaining items before loading more
    private final boolean disableInfiniteScroll;
    private final String layoutName;

    private ResultsAdapter adapter;
    private LayoutManager layoutManager;
    private View emptyView;
    private AlgoliaHelper helper;


    public Hits(Context context, AttributeSet attrs) throws AlgoliaException {
        super(context, attrs);
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Hits, 0, 0);
        try {
            hitsPerPage = styledAttributes.getInt(R.styleable.Hits_hitsPerPage, DEFAULT_HITS_PER_PAGE);
            layoutName = styledAttributes.getString(R.styleable.Hits_itemLayout);
            disableInfiniteScroll = styledAttributes.getBoolean(R.styleable.Hits_disableInfiniteScroll, false);
            int remainingItemsAttribute = styledAttributes.getInt(R.styleable.Hits_remainingItemsBeforeLoading, MISSING_VALUE);
            if (remainingItemsAttribute == MISSING_VALUE) {
                remainingItemsBeforeLoading = DEFAULT_REMAINING_ITEMS;
            } else {
                remainingItemsBeforeLoading = remainingItemsAttribute;
                if (disableInfiniteScroll) {
                    throw new AlgoliaException("You specified both disableInfiniteScroll and remainingItemsBeforeLoading, but they are mutually exclusive.");
                }
            }

        } finally {
            styledAttributes.recycle();
        }

        this.setHasFixedSize(true); // Enables optimisations as the view's width & height are fixed

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

        if (!disableInfiniteScroll) {
            addOnScrollListener(new HitsScrollListener());
        }
    }

    /**
     * Clear the Hits, emptying the underlying data
     */
    public void clear() {
        adapter.clear();
        adapter.notifyDataSetChanged();
    }

    /**
     * Add or replace Results to this widget
     *
     * @param results     A collection of Result objects
     * @param isReplacing if true, will replace the current results
     */
    private void addResults(Collection<Result> results, boolean isReplacing) {
        if (isReplacing) {
            adapter.clear();
        }

        for (Result res : results) {
            adapter.add(res);
        }

        if (isReplacing) {
            adapter.notifyDataSetChanged();
            smoothScrollToPosition(0);
        } else {
            adapter.notifyItemRangeInserted(adapter.getItemCount(), results.size());
        }
    }

    private void updateEmptyView() {
        if (adapter.getItemCount() == 0) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onUpdateView(Collection<Result> results, boolean isReplacing) {
        addResults(results, isReplacing);
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

    public Integer getHitsPerPage() {
        return hitsPerPage;
    }

    public String getLayoutName() {
        return layoutName;
    }

    public void setHelper(AlgoliaHelper helper) {
        this.helper = helper;
    }

    private class HitsScrollListener extends OnScrollListener {
        private int lastItemCount = 0; // Item count after last event
        private boolean currentlyLoading = true; // Are we waiting for new results?

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (!helper.shouldLoadMore()) {
                return;
            }

            int totalItemCount = layoutManager.getItemCount();
            if (totalItemCount < lastItemCount) {
                // we have less elements than before, the count should be reset
                lastItemCount = totalItemCount;
                if (totalItemCount == 0) {
                    // the list is empty -> do nothing until we get more results.
                    currentlyLoading = true;
                    return;
                }
            }

            if (currentlyLoading) {
                if (totalItemCount > lastItemCount) {
                    // the data changed, loading is finished
                    currentlyLoading = false;
                    lastItemCount = totalItemCount;
                }
            } else {
                int lastVisiblePosition = getLastVisibleItemPosition();

                if ((lastVisiblePosition + remainingItemsBeforeLoading > totalItemCount)) {
                    // we are under the loading threshold, let's load more data
                    helper.loadMore();
                    currentlyLoading = true;
                }
            }

        }

        /**
         * Calculates the position of last visible item, notwithstanding the LayoutManager's class
         *
         * @return the last visible item's position in the list
         */
        private int getLastVisibleItemPosition() {
            int lastVisiblePosition = 0;
            if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null);
                // last position = biggest value within the list of positions
                int maxSize = lastVisibleItemPositions[0];
                for (int lastVisibleItemPosition : lastVisibleItemPositions) {
                    if (lastVisibleItemPosition > maxSize) {
                        maxSize = lastVisibleItemPosition;
                    }
                }
                lastVisiblePosition = maxSize;
            } else if (layoutManager instanceof LinearLayoutManager) {
                lastVisiblePosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            return lastVisiblePosition;
        }

    }
}
package com.algolia.instantsearch.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.algolia.instantsearch.R;
import com.algolia.instantsearch.Searcher;
import com.algolia.instantsearch.model.Errors;
import com.algolia.instantsearch.ui.HitsAdapter;
import com.algolia.instantsearch.utils.ItemClickSupport;
import com.algolia.instantsearch.utils.ItemClickSupport.OnItemClickListener;
import com.algolia.instantsearch.utils.ItemClickSupport.OnItemLongClickListener;
import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Query;

import org.json.JSONArray;
import org.json.JSONObject;

public class Hits extends RecyclerView implements AlgoliaWidget {
    public static final int MISSING_VALUE = Integer.MIN_VALUE;
    public static final int DEFAULT_HITS_PER_PAGE = 20;
    public static final int DEFAULT_REMAINING_ITEMS = 5;

    private final Integer hitsPerPage;
    private final int remainingItemsBeforeLoading; // Minimum number of remaining items before loading more
    private final boolean disableInfiniteScroll;
    private final HitsScrollListener scrollListener;

    private final int layoutId;

    private HitsAdapter adapter;

    private LayoutManager layoutManager;
    private View emptyView;
    private Searcher searcher;

    public Hits(Context context, AttributeSet attrs) throws AlgoliaException {
        super(context, attrs);
        if (isInEditMode()) {
            hitsPerPage = 0;
            remainingItemsBeforeLoading = 0;
            disableInfiniteScroll = false;
            layoutId = 0;
            scrollListener = null;
            return;
        }

        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Hits, 0, 0);
        try {
            hitsPerPage = styledAttributes.getInt(R.styleable.Hits_hitsPerPage, DEFAULT_HITS_PER_PAGE);
            layoutId = styledAttributes.getResourceId(R.styleable.Hits_itemLayout, 0);
            disableInfiniteScroll = styledAttributes.getBoolean(R.styleable.Hits_disableInfiniteScroll, false);
            int remainingItemsAttribute = styledAttributes.getInt(R.styleable.Hits_remainingItemsBeforeLoading, MISSING_VALUE);
            if (remainingItemsAttribute == MISSING_VALUE) {
                remainingItemsBeforeLoading = DEFAULT_REMAINING_ITEMS;
            } else {
                remainingItemsBeforeLoading = remainingItemsAttribute;
                if (disableInfiniteScroll) {
                    throw new AlgoliaException(Errors.HITS_INFINITESCROLL);
                }
            }

        } finally {
            styledAttributes.recycle();
        }

        this.setHasFixedSize(true); // Enables optimisations as the view's width & height are fixed

        adapter = new HitsAdapter();
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

        scrollListener = disableInfiniteScroll ? null : new HitsScrollListener();
        if (!disableInfiniteScroll) {
            addOnScrollListener(scrollListener);
        }
    }


    /**
     * Set a listener for click events on child views.
     *
     * @param listener An {@link OnItemClickListener} which should receive these events.
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        ItemClickSupport.addTo(this).setOnItemClickListener(listener);
    }

    /**
     * Set a listener for long click events on child views.
     *
     * @param listener An {@link OnItemLongClickListener} which should receive these events.
     */
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        ItemClickSupport.addTo(this).setOnItemLongClickListener(listener);
    }

    /**
     * Clear the Hits, emptying the underlying data.
     */
    public void clear() {
        adapter.clear();
    }

    /**
     * Get the hit at a given position.
     *
     * @param position the position to look at.
     * @return a JSONObject representing the hit.
     */
    public JSONObject get(int position) {
        return adapter.getItemAt(position);
    }

    /**
     * Add or replace hits to/in this widget.
     *
     * @param hits        A {@link JSONObject} containing hits.
     * @param isReplacing true if the given hits should replace the current hits.
     */
    private void addHits(@Nullable JSONObject hits, boolean isReplacing) {
        if (hits == null) {
            if (isReplacing) {
                adapter.clear();
                scrollListener.setCurrentlyLoading(false);
            }
            return;
        }

        JSONArray resultHits = hits.optJSONArray("hits");

        if (isReplacing) {
            adapter.clear(false);
        }

        for (int i = 0; i < resultHits.length(); ++i) {
            JSONObject hit = resultHits.optJSONObject(i);
            if (hit != null) {
                adapter.add(hit);
            }
        }

        if (isReplacing) {
            adapter.notifyDataSetChanged();
            smoothScrollToPosition(0);
            scrollListener.setCurrentlyLoading(false);
        } else {
            adapter.notifyItemRangeInserted(adapter.getItemCount(), resultHits.length());
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
    public void setSearcher(@NonNull Searcher searcher) {
        this.searcher = searcher;
    }

    @Override
    public void onResults(@Nullable JSONObject hits, boolean isLoadingMore) {
        addHits(hits, !isLoadingMore);
    }

    @Override
    public void onError(Query query, AlgoliaException error) {
        Toast.makeText(getContext(), "Error while searching '" + query.getQuery() + "':" + error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override public void onReset() {
        addHits(null, true);
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

    public Integer getHitsPerPage() {
        return hitsPerPage;
    }

    public int getLayoutId() {
        return layoutId;
    }

    private class HitsScrollListener extends OnScrollListener {
        private int lastItemCount = 0; // Item count after last event
        private boolean currentlyLoading = true; // Are we waiting for new results?

        public void setCurrentlyLoading(boolean currentlyLoading) {
            this.currentlyLoading = currentlyLoading;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (!searcher.shouldLoadMore()) {
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
                    searcher.loadMore();
                    currentlyLoading = true;
                }
            }
        }

        /**
         * Calculate the position of last visible item, notwithstanding the LayoutManager's class.
         *
         * @return the last visible item's position in the list.
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

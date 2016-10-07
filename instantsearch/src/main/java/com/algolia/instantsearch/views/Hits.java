package com.algolia.instantsearch.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.algolia.instantsearch.BindingHelper;
import com.algolia.instantsearch.helpers.InstantSearchHelper;
import com.algolia.instantsearch.R;
import com.algolia.instantsearch.RenderingHelper;
import com.algolia.instantsearch.helpers.Searcher;
import com.algolia.instantsearch.model.Errors;
import com.algolia.instantsearch.model.SearchResults;
import com.algolia.instantsearch.helpers.Highlighter;
import com.algolia.instantsearch.utils.ImageLoadTask;
import com.algolia.instantsearch.utils.ItemClickSupport;
import com.algolia.instantsearch.utils.ItemClickSupport.OnItemClickListener;
import com.algolia.instantsearch.utils.ItemClickSupport.OnItemLongClickListener;
import com.algolia.instantsearch.utils.LayoutViews;
import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Query;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Hits extends RecyclerView implements AlgoliaWidget {
    public static final int DEFAULT_HITS_PER_PAGE = 20;
    public static final int DEFAULT_REMAINING_ITEMS = 5;
    private static final int MISSING_VALUE = Integer.MIN_VALUE;

    private final Integer hitsPerPage;
    private final int remainingItemsBeforeLoading; // Minimum number of remaining items before loading more
    private final HitsScrollListener scrollListener;

    private final int layoutId;

    private HitsAdapter adapter;

    private LayoutManager layoutManager;
    private View emptyView;
    private Searcher searcher;

    public Hits(@NonNull Context context, AttributeSet attrs) throws AlgoliaException {
        super(context, attrs);
        if (isInEditMode()) {
            hitsPerPage = 0;
            remainingItemsBeforeLoading = 0;
            layoutId = 0;
            scrollListener = null;
            return;
        }

        boolean disableInfiniteScroll;
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
     *  @param results        A {@link JSONObject} containing hits.
     * @param isReplacing true if the given hits should replace the current hits.
     */
    private void addHits(@Nullable SearchResults results, boolean isReplacing) {
        if (results == null) {
            if (isReplacing) {
                adapter.clear();
                scrollListener.setCurrentlyLoading(false);
            }
            return;
        }

        JSONArray hits = results.hits;

        if (isReplacing) {
            adapter.clear(false);
        }

        for (int i = 0; i < hits.length(); ++i) {
            JSONObject hit = hits.optJSONObject(i);
            if (hit != null) {
                adapter.add(hit);
            }
        }

        if (isReplacing) {
            adapter.notifyDataSetChanged();
            smoothScrollToPosition(0);
            scrollListener.setCurrentlyLoading(false);
        } else {
            adapter.notifyItemRangeInserted(adapter.getItemCount(), hits.length());
        }
    }

    private void updateEmptyView() {
        if (emptyView == null) {
            return;
        }
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
    public void onResults(SearchResults results, boolean isLoadingMore) {
        addHits(results, !isLoadingMore);
    }

    @Override
    public void onError(@NonNull Query query, @NonNull AlgoliaException error) {
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

    private static class HitsAdapter extends Adapter<HitsAdapter.ViewHolder> implements ImageLoadTask.BitmapListener {

        @NonNull
        private List<JSONObject> hits = new ArrayList<>();
        private final Map<String, Bitmap> bitmaps = new HashMap<>();

        public HitsAdapter() {
            this.hits = new ArrayList<>();
        }

        /**
         * Remove the current hits, notifying observers.
         */
        public void clear() {
            clear(true);
        }

        /**
         * Remove the current hits, potentially notifying observers.
         *
         * @param shouldNotify true if the adapter should notify observers of removal.
         */
        public void clear(boolean shouldNotify) {
            if (shouldNotify) {
                final int previousItemCount = getItemCount();
                hits.clear();
                notifyItemRangeRemoved(0, previousItemCount);
            } else {
                hits.clear();
            }
        }

        public void add(JSONObject result) {
            hits.add(result);
        }

        public JSONObject getItemAt(int position) {
            return hits.get(position);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
            ViewDataBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()), InstantSearchHelper.getItemLayoutId(), parent, false);
            binding.executePendingBindings();
            return new ViewHolder(binding.getRoot());
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final Set<View> mappedViews = holder.viewMap.keySet();
            final List<AlgoliaHitView> hitViews = LayoutViews.findByClass((ViewGroup) holder.itemView, AlgoliaHitView.class);
            final JSONObject hit = hits.get(position);

            // For every AlgoliaHitView that is not bound, trigger onResults
            for (AlgoliaHitView hitView : hitViews) {
                //noinspection SuspiciousMethodCalls: With LayoutViews, we are sure to only find Views
                if (mappedViews.contains(hitView)) {
                    continue;
                }
                hitView.onUpdateView(hit);
            }

            // For every view we have bound, if we can handle its class let's send them the hit
            for (Map.Entry<View, String> entry : holder.viewMap.entrySet()) {
                final View view = entry.getKey();
                final String attributeName = entry.getValue();
                final String attributeValue = hit.optString(attributeName);
                if (view instanceof AlgoliaHitView) {
                    ((AlgoliaHitView) view).onUpdateView(hit);
                } else if (view instanceof EditText) {
                    ((EditText) view).setHint(getHighlightedAttribute(hit, view, attributeName, attributeValue));
                } else if (view instanceof RatingBar) {
                    ((RatingBar) view).setRating(Float.parseFloat(attributeValue));
                } else if (view instanceof ProgressBar) {
                    ((ProgressBar) view).setProgress(Math.round(Float.parseFloat(attributeValue)));
                } else if (view instanceof TextView) {
                    ((TextView) view).setText(getHighlightedAttribute(hit, view, attributeName, attributeValue));
                } else if (view instanceof ImageView) {
                    final Bitmap bitmap = bitmaps.get(attributeValue);
                    final ImageView imageView = (ImageView) view;
                    if (bitmap == null) {
                        new ImageLoadTask(this, imageView).execute(attributeValue);
                    } else {
                        ((ImageView) view).setImageBitmap(bitmap);
                    }
                } else {
                    throw new IllegalStateException(String.format(Errors.ADAPTER_UNKNOWN_VIEW, view.getClass().getCanonicalName()));
                }
            }
        }

        private Spannable getHighlightedAttribute(JSONObject hit, View view, String attributeName, String attributeValue) {
            Spannable attributeText;
            if (RenderingHelper.getDefault().shouldHighlight(attributeName)) {
                final int highlightColor = RenderingHelper.getDefault().getHighlightColor(attributeName);
                attributeText = Highlighter.getDefault().renderHighlightColor(hit, attributeName, highlightColor, view.getContext());
            } else {
                attributeText = new SpannableString(attributeValue);
            }
            return attributeText;
        }

        @Override
        public int getItemCount() {
            return hits.size();
        }

        @Override
        public void onResult(String url, Bitmap bitmap, @NonNull ImageView view) {
            view.setImageBitmap(bitmap);
            bitmaps.put(url, bitmap);
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final Map<View, String> viewMap = new HashMap<>();

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                // Store every annotated view with its attribute name
                for (Map.Entry<Integer, String> entry : BindingHelper.getEntrySet()) {
                    final String attributeName = entry.getValue();
                    final View view = itemView.findViewById(entry.getKey());
                    viewMap.put(view, attributeName);
                }
            }

        }
    }
}

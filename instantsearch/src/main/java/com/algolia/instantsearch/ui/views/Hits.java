package com.algolia.instantsearch.ui.views;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.algolia.instantsearch.R;
import com.algolia.instantsearch.events.ResetEvent;
import com.algolia.instantsearch.helpers.Highlighter;
import com.algolia.instantsearch.helpers.InstantSearch;
import com.algolia.instantsearch.helpers.Searcher;
import com.algolia.instantsearch.model.AlgoliaErrorListener;
import com.algolia.instantsearch.model.AlgoliaResultsListener;
import com.algolia.instantsearch.model.AlgoliaSearcherListener;
import com.algolia.instantsearch.model.Errors;
import com.algolia.instantsearch.model.SearchResults;
import com.algolia.instantsearch.ui.databinding.BindingHelper;
import com.algolia.instantsearch.ui.databinding.RenderingHelper;
import com.algolia.instantsearch.utils.ItemClickSupport;
import com.algolia.instantsearch.utils.ItemClickSupport.OnItemClickListener;
import com.algolia.instantsearch.utils.ItemClickSupport.OnItemLongClickListener;
import com.algolia.instantsearch.utils.JSONUtils;
import com.algolia.instantsearch.utils.LayoutViews;
import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Query;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Displays your search results in a flexible way. Built over a {@link RecyclerView}, it displays a limited window into a large data set of search results.
 */
public class Hits extends RecyclerView implements AlgoliaResultsListener, AlgoliaErrorListener, AlgoliaSearcherListener {
    /** Default amount of hits fetched with each page */
    public static final int DEFAULT_HITS_PER_PAGE = 20;
    /** Default amount of remaining results to display before loading a new page */
    public static final int DEFAULT_REMAINING_ITEMS = 5;

    private static final int MISSING_VALUE = Integer.MIN_VALUE;

    /** The minimum number of items remaining below the fold before loading more. */
    private final int remainingItemsBeforeLoading;

    private final @NonNull Integer hitsPerPage;
    private final int layoutId;

    private @NonNull HitsAdapter adapter;
    private @NonNull LayoutManager layoutManager;
    private @NonNull Searcher searcher;
    private @NonNull InputMethodManager imeManager;

    private @Nullable final InfiniteScrollListener infiniteScrollListener;
    private @Nullable OnScrollListener keyboardListener;
    private @Nullable View emptyView;

    /**
     * Constructs a new Hits with the given context's theme and the supplied attribute set.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     */
    public Hits(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode()) {
            hitsPerPage = 0;
            remainingItemsBeforeLoading = 0;
            layoutId = 0;
            infiniteScrollListener = null;
            //noinspection ConstantConditions Edit mode initialization
            adapter = null;
            //noinspection ConstantConditions
            searcher = null;
            //noinspection ConstantConditions
            layoutManager = null;
            //noinspection ConstantConditions
            imeManager = null;
            return;
        }

        boolean infiniteScroll;
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Hits, 0, 0);
        try {
            hitsPerPage = styledAttributes.getInt(R.styleable.Hits_hitsPerPage, DEFAULT_HITS_PER_PAGE);
            layoutId = styledAttributes.getResourceId(R.styleable.Hits_itemLayout, 0);
            infiniteScroll = styledAttributes.getBoolean(R.styleable.Hits_infiniteScroll, true);
            if (styledAttributes.getBoolean(R.styleable.Hits_autoHideKeyboard, false)) {
                enableKeyboardAutoHiding();
            }

            int remainingItemsAttribute = styledAttributes.getInt(R.styleable.Hits_remainingItemsBeforeLoading, MISSING_VALUE);
            if (remainingItemsAttribute == MISSING_VALUE) {
                remainingItemsBeforeLoading = DEFAULT_REMAINING_ITEMS;
            } else {
                remainingItemsBeforeLoading = remainingItemsAttribute;
                if (!infiniteScroll) {
                    throw new IllegalStateException(Errors.HITS_INFINITESCROLL_BUT_REMAINING);
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

        imeManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        layoutManager = new LinearLayoutManager(context);
        setLayoutManager(layoutManager);

        infiniteScrollListener = infiniteScroll ? new InfiniteScrollListener() : null;
        if (infiniteScroll) {
            addOnScrollListener(infiniteScrollListener);
        }
    }


    /**
     * Sets a listener for click events on child views.
     *
     * @param listener An {@link OnItemClickListener OnItemLongClickListener} which should receive these events.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public void setOnItemClickListener(OnItemClickListener listener) {
        //noinspection deprecation legitimate use
        ItemClickSupport.addTo(this).setOnItemClickListener(listener);
    }

    /**
     * Sets a listener for long click events on child views.
     *
     * @param listener An {@link OnItemLongClickListener OnItemLongClickListener} which should receive these events.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        //noinspection deprecation legitimate use
        ItemClickSupport.addTo(this).setOnItemLongClickListener(listener);
    }

    /**
     * Clears the Hits, emptying the underlying data.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public void clear() {
        adapter.clear();
    }

    /**
     * Gets the hit at a given position.
     *
     * @param position the position to look at.
     * @return a JSONObject representing the hit.
     */
    public JSONObject get(int position) {
        return adapter.getItemAt(position);
    }

    /**
     * Starts closing the keyboard when the hits are scrolled.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public void enableKeyboardAutoHiding() {
        keyboardListener = new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dx != 0 || dy != 0) {
                    imeManager.hideSoftInputFromWindow(
                            Hits.this.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        };
        addOnScrollListener(keyboardListener);
    }

    /**
     * Stops closing the keyboard when the hits are scrolled.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public void disableKeyboardAutoHiding() {
        removeOnScrollListener(keyboardListener);
        keyboardListener = null;
    }

    /**
     * Adds or replaces hits to/in this widget.
     *
     * @param results     A {@link JSONObject} containing hits.
     * @param isReplacing {@code true} if the given hits should replace the current hits.
     */
    private void addHits(@Nullable SearchResults results, boolean isReplacing) {
        if (results == null) {
            if (isReplacing) {
                clear();
                if (infiniteScrollListener != null) {
                    infiniteScrollListener.setCurrentlyLoading(false);
                }
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
            if (infiniteScrollListener != null) {
                infiniteScrollListener.setCurrentlyLoading(false);
            }
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
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetachedFromWindow() {
        EventBus.getDefault().unregister(this);
        super.onDetachedFromWindow();
    }

    @Override
    public void initWithSearcher(@NonNull Searcher searcher) {
        this.searcher = searcher;
    }

    @Override
    public void onResults(@NonNull SearchResults results, boolean isLoadingMore) {
        addHits(results, !isLoadingMore);
    }

    @Override
    public void onError(@NonNull Query query, @NonNull AlgoliaException error) {
        Log.e("Algolia|Hits", "Error while searching '" + query.getQuery() + "':" + error.getMessage());
    }

    @Subscribe
    public void onReset(ResetEvent event) {
        addHits(null, true);
    }

    /**
     * Specify an empty View to display instead of these Hits when they are empty.
     * By default, we will search for a view with id {@code @android:id/empty} and will use it if it exists.
     *
     * @param emptyView the View you want to display when Hits are empty.
     */
    public void setEmptyView(@Nullable View emptyView) {
        this.emptyView = emptyView;
    }

    /**
     * Get the current hitsPerPage settings.
     *
     * @return the value of the attribute hitsPerPage if specified, else DEFAULT_HITS_PER_PAGE.
     */
    @NonNull public Integer getHitsPerPage() {
        return hitsPerPage;
    }

    /**
     * Get the itemLayout layout identifier.
     *
     * @return the identifier of the layout referenced in itemLayout if specified, else 0.
     */
    public int getLayoutId() {
        return layoutId;
    }

    private class InfiniteScrollListener extends OnScrollListener {
        private int lastItemCount = 0; // Item count after last event
        private boolean currentlyLoading = true; // Are we waiting for new results?

        void setCurrentlyLoading(boolean currentlyLoading) {
            this.currentlyLoading = currentlyLoading;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (!searcher.hasMoreHits()) {
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

    private static class HitsAdapter extends Adapter<HitsAdapter.ViewHolder> {

        @NonNull
        private List<JSONObject> hits = new ArrayList<>();
        private SparseArray<Drawable> placeholders = new SparseArray<>();

        HitsAdapter() {
            this.hits = new ArrayList<>();
        }

        void clear() {
            clear(true);
        }

        void clear(boolean shouldNotify) {
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

        JSONObject getItemAt(int position) {
            return hits.get(position);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
            ViewDataBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()), InstantSearch.getItemLayoutId(), parent, false);
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
                final String attributeValue = JSONUtils.getStringFromJSONPath(hit, attributeName);
                if (view instanceof AlgoliaHitView) {
                    ((AlgoliaHitView) view).onUpdateView(hit);
                } else if (view instanceof EditText) {
                    ((EditText) view).setHint(getHighlightedAttribute(hit, view, attributeName, attributeValue));
                } else if (view instanceof RatingBar) {
                    ((RatingBar) view).setRating(getFloatValue(attributeValue));
                } else if (view instanceof ProgressBar) {
                    ((ProgressBar) view).setProgress(Math.round(getFloatValue(attributeValue)));
                } else if (view instanceof TextView) {
                    ((TextView) view).setText(getHighlightedAttribute(hit, view, attributeName, attributeValue));
                } else if (view instanceof ImageView) {
                    final Activity activity = getActivity(view);
                    if (activity == null || Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()) {
                        continue;
                    }

                    final ImageView imageView = (ImageView) view;
                    final int viewId = imageView.getId();
                    Drawable placeholder = placeholders.get(viewId);
                    if (placeholder == null) {
                        placeholder = imageView.getDrawable();
                        placeholders.put(viewId, placeholder);
                    }
                    Glide.with(activity).applyDefaultRequestOptions(new RequestOptions().fitCenter().placeholder(placeholder)).load(attributeValue).into(imageView);
                } else {
                    throw new IllegalStateException(String.format(Errors.ADAPTER_UNKNOWN_VIEW, view.getClass().getCanonicalName()));
                }
            }
        }

        private float getFloatValue(String attributeValue) {
            return attributeValue != null ? Float.parseFloat(attributeValue) : 0;
        }

        private
        @Nullable
        Spannable getHighlightedAttribute(@NonNull JSONObject hit, @NonNull View view, @NonNull String attribute, @Nullable String attributeValue) {
            Spannable attributeText;
            if (RenderingHelper.getDefault().shouldHighlight(attribute)) {
                final int highlightColor = RenderingHelper.getDefault().getHighlightColor(attribute);
                attributeText = Highlighter.getDefault().renderHighlightColor(hit, attribute, highlightColor, view.getContext());
            } else {
                attributeText = attributeValue != null ? new SpannableString(attributeValue) : null;
            }
            return attributeText;
        }

        @Override
        public int getItemCount() {
            return hits.size();
        }

        private Activity getActivity(View view) {
            Context context = view.getContext();
            while (context instanceof ContextWrapper) {
                if (context instanceof Activity) {
                    return (Activity) context;
                }
                context = ((ContextWrapper) context).getBaseContext();
            }
            Log.e("Algolia|Hits", "Error: Could not get activity from View.");
            return null;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final Map<View, String> viewMap = new HashMap<>();

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                // Store every annotated view with its attribute name
                final SparseArray<String> attributes = BindingHelper.getBindings();
                for (int i = 0; i < attributes.size(); i++) {
                    viewMap.put(itemView.findViewById(attributes.keyAt(i)), attributes.valueAt(i));
                }
            }

        }
    }
}

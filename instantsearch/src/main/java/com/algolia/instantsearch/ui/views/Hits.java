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
import com.algolia.instantsearch.core.events.ResetEvent;
import com.algolia.instantsearch.core.helpers.Highlighter;
import com.algolia.instantsearch.core.helpers.Searcher;
import com.algolia.instantsearch.core.model.AlgoliaErrorListener;
import com.algolia.instantsearch.core.model.AlgoliaResultsListener;
import com.algolia.instantsearch.core.model.AlgoliaSearcherListener;
import com.algolia.instantsearch.core.model.Errors;
import com.algolia.instantsearch.core.model.SearchResults;
import com.algolia.instantsearch.ui.databinding.BindingHelper;
import com.algolia.instantsearch.ui.databinding.RenderingHelper;
import com.algolia.instantsearch.ui.utils.ItemClickSupport;
import com.algolia.instantsearch.ui.utils.ItemClickSupport.OnItemClickListener;
import com.algolia.instantsearch.ui.utils.ItemClickSupport.OnItemLongClickListener;
import com.algolia.instantsearch.core.utils.JSONUtils;
import com.algolia.instantsearch.ui.utils.LayoutViews;
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
    /**
     * Default amount of remaining results to display before loading a new page
     */
    public static final int DEFAULT_REMAINING_ITEMS = 5;

    private static final int MISSING_VALUE = Integer.MIN_VALUE;

    /**
     * The minimum number of items remaining below the fold before loading more.
     */
    private final int remainingItemsBeforeLoading;

    protected int layoutId;

    @NonNull
    protected HitsAdapter adapter;
    @NonNull
    private LayoutManager layoutManager;
    @SuppressWarnings("NullableProblems" /* late init*/)
    @NonNull
    private Searcher searcher;
    @NonNull
    private InputMethodManager imeManager;

    @Nullable
    protected final InfiniteScrollListener infiniteScrollListener;
    @Nullable
    private OnScrollListener keyboardListener;
    @Nullable
    private View emptyView;

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

            BindingHelper.setVariantForView(this, attrs); //TODO: Can I use this as default variant for all subviews?
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
    protected void addHits(@Nullable SearchResults results, boolean isReplacing) {
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

    protected
    @Nullable
    Spannable getHighlightedAttribute(@NonNull JSONObject hit, @NonNull View view, @NonNull String attribute, @Nullable String attributeValue) {
        Spannable attributeText = null;
        if (attributeValue != null) {
            if (RenderingHelper.getDefault().shouldHighlight(view, attribute)) {
                final int highlightColor = RenderingHelper.getDefault().getHighlightColor(view, attribute);
                attributeText = Highlighter.getDefault()
                        .setInput(hit, attribute, BindingHelper.getPrefix(view), BindingHelper.getSuffix(view), false)
                        .setStyle(highlightColor).render();
            } else {
                attributeText = new SpannableString(attributeValue);
            }
        }
        return attributeText;
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
                    setCurrentlyLoading(true);
                    return;
                }
            }

            if (currentlyLoading) {
                if (totalItemCount > lastItemCount) {
                    // the data changed, loading is finished
                    setCurrentlyLoading(false);
                    lastItemCount = totalItemCount;
                }
            } else {
                int lastVisiblePosition = getLastVisibleItemPosition();

                if ((lastVisiblePosition + remainingItemsBeforeLoading > totalItemCount)) {
                    // we are under the loading threshold, let's load more data
                    searcher.loadMore();
                    setCurrentlyLoading(true);
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

    private class HitsAdapter extends Adapter<HitsAdapter.ViewHolder> {

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
                    LayoutInflater.from(parent.getContext()), layoutId, parent, false);
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
                if (attributeName == null) {
                    Log.d("Hits", "View " + view.toString() + " is bound to null attribute, skipping");
                    continue;
                }
                final String attributeValue = JSONUtils.getStringFromJSONPath(hit, attributeName);
                if (attributeValue == null) {
                    Log.d("Hits", "View " + view.toString() + " is bound to attribute " + attributeName + " missing in JSON, skipping");
                    continue;
                }
                final String fullAttribute = BindingHelper.getFullAttribute(view, attributeValue);
                if (view instanceof AlgoliaHitView) {
                    ((AlgoliaHitView) view).onUpdateView(hit);
                } else if (view instanceof EditText) {
                    ((EditText) view).setHint(getHighlightedAttribute(hit, view, attributeName, fullAttribute));
                } else if (view instanceof RatingBar) {
                    ((RatingBar) view).setRating(getFloatValue(fullAttribute));
                } else if (view instanceof ProgressBar) {
                    ((ProgressBar) view).setProgress(Math.round(getFloatValue(fullAttribute)));
                } else if (view instanceof TextView) {
                    ((TextView) view).setText(getHighlightedAttribute(hit, view, attributeName, fullAttribute));
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
                    final RequestOptions requestOptions = new RequestOptions().placeholder(placeholder);
                    switch (imageView.getScaleType()) {
                        case CENTER_CROP:
                            requestOptions.centerCrop();
                            break;
                        case FIT_CENTER:
                            requestOptions.fitCenter();
                            break;
                        case FIT_XY:
                        case FIT_START:
                        case FIT_END:
                        case MATRIX:
                        case CENTER:
                            break;
                        case CENTER_INSIDE:
                        default:
                            requestOptions.centerInside();
                            break;
                    }
                    Glide.with(activity).applyDefaultRequestOptions(requestOptions).load(fullAttribute).into(imageView);
                } else {
                    throw new IllegalStateException(String.format(Errors.ADAPTER_UNKNOWN_VIEW, view.getClass().getCanonicalName()));
                }
            }
        }

        private float getFloatValue(String attributeValue) {
            return attributeValue != null ? Float.parseFloat(attributeValue) : 0;
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
            private final Map<View, String> viewMap = new HashMap<>();
            // needed to differentiate initial key value from Pair{null,null} which is legitimate
            private final String defaultValue = "ADefaultValueForHitsVariant";

            ViewHolder(@NonNull View itemView) {
                super(itemView);

                String indexVariant = defaultValue;

                // Get the index and variant for this layout
                final List<View> views = LayoutViews.findAny((ViewGroup) itemView);
                for (View view : views) {
                    if (!BindingHelper.isBound(view.getId())) {
                        continue; // If the view is not bound, we can skip it
                    }
                    if (view instanceof AlgoliaHitView) { //TODO: Test variants + AlgoliaHitView
                        continue;
                    }

                    String viewIndexVariant = BindingHelper.getVariantForView(view);
                    final boolean isSameVariant = viewIndexVariant == null ? indexVariant == null : viewIndexVariant.equals(indexVariant);
                    if (!defaultValue.equals(indexVariant) && !isSameVariant) {
                        throw new IllegalStateException("Hits found two conflicting variants within its views: " + indexVariant + " / " + viewIndexVariant + ".");
                    }
                    indexVariant = viewIndexVariant;
                }

                // Store every annotated view for indexVariant with its attribute name
                final HashMap<Integer, String> attributes = BindingHelper.getBindings(indexVariant);
                if (attributes != null) { // Ensure we have at least some bindings
                    for (Map.Entry<Integer, String> entry : attributes.entrySet()) {
                        if (entry.getValue() != null) { // attribute can be null e.g. if view is a Hits
                            viewMap.put(itemView.findViewById(entry.getKey()), entry.getValue());
                        }
                    }
                }
            }
        }
    }
}

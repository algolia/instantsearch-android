package com.algolia.instantsearch.ui;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.algolia.instantsearch.BindingHelper;
import com.algolia.instantsearch.InstantSearchHelper;
import com.algolia.instantsearch.RenderingHelper;
import com.algolia.instantsearch.model.Errors;
import com.algolia.instantsearch.utils.ImageLoadTask;
import com.algolia.instantsearch.utils.LayoutViews;
import com.algolia.instantsearch.views.AlgoliaHitView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HitsAdapter extends RecyclerView.Adapter<HitsAdapter.ViewHolder> implements ImageLoadTask.BitmapListener {

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

    @NonNull @Override
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
                ((EditText) view).setHint(attributeValue);
            } else if (view instanceof RatingBar) { //TODO SeekBar?
                ((RatingBar) view).setRating(Float.parseFloat(attributeValue));
            } else if (view instanceof TextView) {
                final TextView textView = (TextView) view;
                if (RenderingHelper.getDefault().shouldHighlight(attributeName)) {
                    final int highlightColor = RenderingHelper.getDefault().getHighlightColor(attributeName);
                    textView.setText(Highlighter.getDefault().renderHighlightColor(hit, attributeName, highlightColor, view.getContext()));
                } else {
                    textView.setText(attributeValue);
                }
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

    @Override
    public int getItemCount() {
        return hits.size();
    }

    @Override
    public void onResult(String url, Bitmap bitmap, @NonNull ImageView view) {
        view.setImageBitmap(bitmap);
        bitmaps.put(url, bitmap);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final Map<View, String> viewMap = new HashMap<>();

        public ViewHolder(@NonNull View itemView) {
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

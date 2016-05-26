package com.algolia.instantsearch.ui;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.algolia.instantsearch.AlgoliaHelper;
import com.algolia.instantsearch.ImageLoadTask;
import com.algolia.instantsearch.RenderingHelper;
import com.algolia.instantsearch.model.Result;
import com.algolia.instantsearch.views.AlgoliaAttributeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder> {

    private List<Result> results = new ArrayList<>();
    private ViewGroup parent;

    public ResultsAdapter() {
        this(new ArrayList<Result>());
    }

    public ResultsAdapter(List<Result> dataSet) {
        results = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        this.parent = parent;
        ViewDataBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), AlgoliaHelper.getItemLayoutId(), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // For every view we have, if we can handle its type let's use the result attribute
        for (Map.Entry<View, String> entry : holder.viewMap.entrySet()) {
            final View view = entry.getKey();
            final String attributeName = entry.getValue();
            final Result result = results.get(position);
            final String attributeValue = result.get(attributeName);

            if (view instanceof AlgoliaAttributeView) {
                ((AlgoliaAttributeView) view).onUpdateView(result);
            } else if (view instanceof EditText) {
                ((EditText) view).setHint(attributeValue);
            } else if (view instanceof TextView) {
                final TextView textView = (TextView) view;
                if (RenderingHelper.shouldHighlight(attributeName)) {
                    final int highlightColor = RenderingHelper.getHighlightColor(attributeName);
                    textView.setText(Highlighter.renderHighlightColor(result, attributeName, highlightColor, view.getContext()));
                } else {
                    textView.setText(result.get(attributeName));
                }
            } else if (view instanceof ImageView) {
                final ImageView imageView = (ImageView) view;
                new ImageLoadTask(imageView).execute(attributeValue);
            } else {
                throw new RuntimeException("I was not told how to handle " + view.getClass().getCanonicalName() + "s.");
            }
        }
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void clear() {
        results.clear();
    }

    public void add(Result result) {
        results.add(result);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final Map<View, String> viewMap = new HashMap<>();

        public ViewHolder(View itemView) {
            super(itemView);

            // Store every annotated view with its attribute name
            for (Map.Entry<Integer, String> entry : AlgoliaHelper.getEntrySet()) {
                final String attributeName = entry.getValue();
                final View view = itemView.findViewById(entry.getKey());
                viewMap.put(view, attributeName);
            }
        }
    }
}

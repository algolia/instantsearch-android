package com.algolia.instantsearch.helper.databinding;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.algolia.instantsearch.R;
import com.algolia.instantsearch.helper.AlgoliaHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder> {
    private ViewGroup parent;

    private List<Result> results = new ArrayList<>();

    public ResultsAdapter() {
        this(new ArrayList<Result>());
    }

    public ResultsAdapter(List<Result> dataSet) {
        results = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.hits_item, parent, false);
        this.parent = parent;
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // For every view we have, if we can handle its type let's use the result attribute
        for (Map.Entry<String, View> entry : holder.viewMap.entrySet()) {
            final View view = entry.getValue();
            final String attributeName = entry.getKey();
            final String attributeValue = results.get(position).get(attributeName);
            final String viewName = parent.getResources().getResourceEntryName(view.getId());

            if (view instanceof EditText) {
                ((EditText) view).setHint(attributeValue);
            } else if (view instanceof TextView) {
                final TextView textView = (TextView) view;
                final CharSequence old = textView.getText();
                textView.setText(attributeValue);
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
        HashMap<String, View> viewMap = new HashMap<>();

        public ViewHolder(View itemView) {
            super(itemView);

            // Store every annotated view with its attribute name
            for (Map.Entry<Integer, String> entry : AlgoliaHelper.getEntrySet()) {
                final String attributeName = entry.getValue();
                final View view = itemView.findViewById(entry.getKey());
                viewMap.put(attributeName, view);
            }
        }
    }
}

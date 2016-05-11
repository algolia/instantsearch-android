package com.algolia.instantsearch.helper.databinding;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.algolia.instantsearch.R;

import java.util.ArrayList;
import java.util.List;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder> {
    private List<Result> results = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }

    public ResultsAdapter() {
        this(new ArrayList<Result>());
    }
    public ResultsAdapter(List<Result> dataSet) {
        results = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_default, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(results.get(position).title);
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


}

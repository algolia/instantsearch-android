package com.algolia.instantsearch.helper.databinding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ResultsAdapter extends ArrayAdapter<Result> {
    private List<Result> results = new ArrayList<>();

    public static class ViewHolder {
        TextView title;
    }

    public ResultsAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
        results = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public Result getItem(int position) {
        return results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Result r = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title = ((TextView) convertView.findViewById(android.R.id.text1));
        viewHolder.title.setText(r.title);

        return convertView;
    }
}

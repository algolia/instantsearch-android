package com.algolia.instantsearch.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.algolia.instantsearch.Searcher;
import com.algolia.instantsearch.model.NumericFilter;
import com.algolia.search.saas.Query;

public class FilterResultsFragment extends DialogFragment {
    public static final String TAG = "FilterResultsFragment";
    private Searcher searcher;

    private LinearLayout layout;
    public static final String TEXT_SEEKBAR_CHANGED = "At least <b>%d</b> %s";
    public static final String TEXT_SEEKBAR_DEFAULT = "Filter minimum %s";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (searcher == null) {
            throw new IllegalStateException("You need to call setSearcher() before you can use a FilterResultsFragment.");
        }

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        addSeekBar("views", 0, 1000000000, 100);
        b.setTitle("Filter results").setView(layout)
                .setMessage("Use these settings to refine the current results.")
                .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        searcher.search();
                        Log.e("PLN", "filters:" + searcher.getQuery().getFilters());
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "Cancelled filtering.", Toast.LENGTH_SHORT).show();
                    }
                });
        return b.create();
    }

    private void addSeekBar(final String attribute, final long minValue, final long maxValue, final int steps) {
        addSeekBar(attribute, attribute, minValue, maxValue, steps);
    }

    private void addSeekBar(final String attribute, final String name, final long minValue, final long maxValue, final int steps) {

        final TextView tv = new TextView(getActivity());
        final SeekBar seekBar = new SeekBar(getActivity());
        final NumericFilter currentFilter = searcher.numericFilterMap.get(attribute);

        if (currentFilter != null && currentFilter.value != 0) {
            final long progressValue = (currentFilter.value - minValue) * steps / (maxValue - minValue);
            seekBar.setProgress((int) progressValue);
        }
        seekBar.setMax(steps);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                onUpdate(seekBar);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                onUpdate(seekBar);
            }

            private void onUpdate(final SeekBar seekBar) {
                final long actualValue = updateProgressText(tv, seekBar, name, minValue, maxValue, steps);
                updateFilters(attribute, actualValue);
            }
        });
        tv.setPadding(seekBar.getPaddingLeft(), tv.getPaddingTop() + 50, tv.getPaddingRight(), tv.getPaddingBottom()); // TODO: Better way to fine tune layout?

        updateProgressText(tv, currentFilter != null ? currentFilter : new NumericFilter(name, 0, NumericFilter.OPERATOR_GT), minValue, maxValue, steps);

        layout.addView(tv);
        layout.addView(seekBar);
    }

    private long updateProgressText(TextView textView, NumericFilter filter, long minValue, long maxValue, int steps) {
        final long progress = (filter.value - minValue) * steps / (maxValue - minValue);
        textView.setText(progress == 0 ? String.format(TEXT_SEEKBAR_DEFAULT, filter.attribute) : Html.fromHtml(String.format(TEXT_SEEKBAR_CHANGED, filter.value, filter.attribute)));
        return filter.value;
    }

    private long updateProgressText(TextView textView, SeekBar seekBar, String name, long minValue, long maxValue, int steps) {
        int progress = seekBar.getProgress();
        final long actualValue = minValue + progress * (maxValue - minValue) / steps;
        textView.setText(progress == 0 ? String.format(TEXT_SEEKBAR_DEFAULT, name) : Html.fromHtml(String.format(TEXT_SEEKBAR_CHANGED, actualValue, name)));
        return actualValue;
    }

    private void updateFilters(String attribute, long value) {
        StringBuilder filters = new StringBuilder();
        searcher.numericFilterMap.put(attribute, new NumericFilter(attribute, value, NumericFilter.OPERATOR_GT));

        final Query query = searcher.getQuery();
        for (NumericFilter numericFilter : searcher.numericFilterMap.values()) {
            if (filters.length() > 0) {
                filters.append(" AND ");
            }
            filters.append(numericFilter.toString());
        }
        query.setFilters(filters.toString()); //FIXME: How can I avoid erasing existing filters before adding my numericRefinements?
    }

    public FilterResultsFragment setSearcher(Searcher searcher) {
        this.searcher = searcher;
        return this;
    }
}

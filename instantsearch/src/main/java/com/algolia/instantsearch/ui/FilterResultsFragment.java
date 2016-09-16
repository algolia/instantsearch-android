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

public class FilterResultsFragment extends DialogFragment {
    public static final String TAG = "FilterResultsFragment";
    private Searcher searcher;

    private LinearLayout layout;

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

    private void addSeekBar(final String attribute, final double minValue, final double maxValue, final int steps) {
        addSeekBar(attribute, attribute, minValue, maxValue, steps);
    }

    private void addSeekBar(final String attribute, final String name, final double minValue, final double maxValue, final int steps) {

        final TextView tv = new TextView(getActivity());
        final SeekBar seekBar = new SeekBar(getActivity());
        final NumericFilter currentFilter = searcher.getNumericFilter(attribute);

        if (currentFilter != null && currentFilter.value != 0) {
            final int progressValue = (int) ((currentFilter.value - minValue) * steps / (maxValue - minValue));
            seekBar.setProgress(progressValue);
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
                final double actualValue = updateProgressText(tv, seekBar, name, minValue, maxValue, steps);
                searcher.addNumericFilter(new NumericFilter(attribute, actualValue, NumericFilter.OPERATOR_GT));
            }
        });
        tv.setPadding(seekBar.getPaddingLeft(), tv.getPaddingTop() + 50, tv.getPaddingRight(), tv.getPaddingBottom()); // TODO: Better way to fine tune layout?

        updateProgressText(tv, currentFilter != null ? currentFilter : new NumericFilter(name, minValue, NumericFilter.OPERATOR_GT), minValue);

        layout.addView(tv);
        layout.addView(seekBar);
    }

    private double updateProgressText(final TextView textView, final NumericFilter filter, final double minValue) {
        updateProgressText(textView, filter.attribute, filter.value, minValue);
        return filter.value;
    }

    private double updateProgressText(final TextView textView, final SeekBar seekBar, final String name, final double minValue, final double maxValue, int steps) {
        int progress = seekBar.getProgress();
        final double value = minValue + progress * (maxValue - minValue) / steps;
        updateProgressText(textView, name, value, minValue);
        return value;
    }

    private void updateProgressText(final TextView textView, final String name, final double value, final double minValue) {
        if (value == minValue) {
            final String prefix = "Filter ";
            final String operatorMeaning = "minimum";
            textView.setText(String.format(prefix + operatorMeaning + " %s", name));
        } else {
            final String prefix = "At least <b>";
            final String suffix = "</b> %s";
            final String html;
            if (value == (long) value) {
                html = String.format(prefix + "%d" + suffix, (long) value, name);
            } else {
                html = String.format(prefix + "%.1f" + suffix, value, name);
            }
            textView.setText(Html.fromHtml(html));
        }
    }

    public FilterResultsFragment setSearcher(final Searcher searcher) {
        this.searcher = searcher;
        return this;
    }
}

package com.algolia.instantsearch.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.algolia.instantsearch.R;
import com.algolia.instantsearch.Searcher;
import com.algolia.instantsearch.model.NumericFilter;

public class FilterResultsFragment extends DialogFragment { //TODO: See display on tablet
    public static final String TAG = "FilterResultsFragment";
    private Searcher searcher;

    private LinearLayout layout;
    private Activity activity;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (searcher == null) {
            throw new IllegalStateException("You need to call with(activity, searcher) before you can show a FilterResultsFragment.");
        }

        final FragmentActivity activity = getActivity();
        AlertDialog.Builder b = new AlertDialog.Builder(activity);
        b.setTitle("Filter results").setView(layout)
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
                        Toast.makeText(activity, "Cancelled filtering.", Toast.LENGTH_SHORT).show();
                    }
                });
        return b.create();
    }

    //TODO: get max from results
    public FilterResultsFragment addSeekBar(final String attribute, final double minValue, final double maxValue, final int steps) {
        return addSeekBar(attribute, attribute, minValue, maxValue, steps);
    }

    public FilterResultsFragment addSeekBar(final String attribute, final String name, final double minValue, final double maxValue, final int steps) {
        checkWith();
        Activity activity = this.activity != null ? this.activity : getActivity();
        LayoutInflater inflater = activity.getLayoutInflater();
        final View seekBarLayout = inflater.inflate(R.layout.dialog_seekbar, null);

        final TextView tv = (TextView) seekBarLayout.findViewById(R.id.dialog_seekbar_text);
        final SeekBar seekBar = (SeekBar) seekBarLayout.findViewById(R.id.dialog_seekbar_bar);
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
                final double actualValue = updateSeekBarText(tv, seekBar, name, minValue, maxValue, steps);
                searcher.addNumericFilter(new NumericFilter(attribute, actualValue, NumericFilter.OPERATOR_GT));
            }
        });

        updateSeekBarText(tv, currentFilter != null ? currentFilter : new NumericFilter(name, minValue, NumericFilter.OPERATOR_GT), minValue);
        layout.addView(seekBarLayout);
        return this;
    }

    public FilterResultsFragment addCheckBox(final String attribute, boolean checkedIsTrue) {
        return addCheckBox(attribute, attribute, checkedIsTrue);
    }

    public FilterResultsFragment addCheckBox(final String attribute, final String name, final boolean checkedIsTrue) {
        checkWith();
        Activity activity = this.activity != null ? this.activity : getActivity();
        LayoutInflater inflater = activity.getLayoutInflater();
        final View checkBoxLayout = inflater.inflate(R.layout.dialog_checkbox, null);

        final TextView tv = (TextView) checkBoxLayout.findViewById(R.id.dialog_checkbox_text);
        final CheckBox checkBox = (CheckBox) checkBoxLayout.findViewById(R.id.dialog_checkbox_box);
        final Boolean currentFilter = searcher.getBooleanFilter(attribute);

        if (currentFilter != null) {
            checkBox.setChecked(currentFilter);
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    searcher.addBooleanFilter(attribute, checkedIsTrue);
                } else {
                    searcher.removeBooleanFilter(attribute);
                }
            }
        });
        tv.setText(name != null ? name : attribute);
        layout.addView(checkBoxLayout);
        return this;
    }

    private double updateSeekBarText(final TextView textView, final NumericFilter filter, final double minValue) {
        updateSeekBarText(textView, filter.attribute, filter.value, minValue);
        return filter.value;
    }

    private double updateSeekBarText(final TextView textView, final SeekBar seekBar, final String name, final double minValue, final double maxValue, int steps) {
        int progress = seekBar.getProgress();
        final double value = minValue + progress * (maxValue - minValue) / steps;
        updateSeekBarText(textView, name, value, minValue);
        return value;
    }

    private void updateSeekBarText(final TextView textView, final String name, final double value, final double minValue) {
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

    public FilterResultsFragment with(Activity activity, Searcher searcher) { //FIXME: Bad DX
        this.activity = activity;
        this.searcher = searcher;
        layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);
        return this;
    }

    private void checkWith() {
        if (searcher == null || activity == null) {
            throw new RuntimeException("You need to prepare the fragment by calling with(activity, searcher) before you can use this method.");
        }
    }
}
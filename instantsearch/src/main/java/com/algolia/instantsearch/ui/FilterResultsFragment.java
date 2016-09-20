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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.algolia.instantsearch.R;
import com.algolia.instantsearch.Searcher;
import com.algolia.instantsearch.model.FacetStat;
import com.algolia.instantsearch.model.NumericFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterResultsFragment extends DialogFragment { //TODO: See display on tablet
    //FIXME: Default constructor + setArguments() http://stackoverflow.com/questions/12062946/why-do-i-want-to-avoid-non-default-constructors-in-fragments
    public static final String TAG = "FilterResultsFragment";
    private Searcher searcher;

    private List<SeekBarRequirements> futureSeekBars = new ArrayList<>();
    private List<CheckBoxRequirements> futureCheckBoxes = new ArrayList<>();
    private Map<Integer, View> views = new HashMap<>();
    private int filterCount = 0;

    private Activity activity;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (searcher == null) {
            throw new IllegalStateException("You need to call with(activity, searcher) before you can show a FilterResultsFragment.");
        }

        final FragmentActivity activity = getActivity();
        AlertDialog.Builder b = new AlertDialog.Builder(activity);
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);

        for (SeekBarRequirements seekBar : futureSeekBars) {
            seekBar.create();
        }
        futureSeekBars.clear();

        for (CheckBoxRequirements checkBox : futureCheckBoxes) {
            checkBox.create();
        }
        futureCheckBoxes.clear();

        for (int i = 0; i < views.size(); i++) {
            View v = views.get(i);
            final ViewParent parent = v.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(v);
            }
            layout.addView(v);
        }

        b.setTitle("Filter results").setView(layout)
                .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        searcher.search();
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

    public FilterResultsFragment addSeekBar(final String attribute, final int steps) {
        return addSeekBar(attribute, attribute, null, null, steps);
    }

    public FilterResultsFragment addSeekBar(final String attribute, final String name, final int steps) {
        return addSeekBar(attribute, name, null, null, steps);
    }

    public FilterResultsFragment addSeekBar(final String attribute, final Double min, final Double max, final int steps) {
        return addSeekBar(attribute, attribute, min, max, steps);
    }

    public FilterResultsFragment addSeekBar(final String attribute, final String name, final Double min, final Double max, final int steps) {
        futureSeekBars.add(new SeekBarRequirements(attribute, name, min, max, steps, filterCount++));
        searcher.addFacet(attribute);
        return this;
    }

    public FilterResultsFragment addCheckBox(final String attribute, boolean checkedIsTrue) {
        return addCheckBox(attribute, attribute, checkedIsTrue);
    }

    public FilterResultsFragment addCheckBox(final String attribute, final String name, final boolean checkedIsTrue) {
        futureCheckBoxes.add(new CheckBoxRequirements(attribute, name, checkedIsTrue, filterCount++));
        return this;
    }

    private void createSeekBar(SeekBarRequirements requirements) {
        checkWith();

        final String attribute = requirements.attribute;
        final String name = requirements.name;
        final double minValue = requirements.min;
        final double maxValue = requirements.max;
        final int steps = requirements.steps;

        final View seekBarLayout = getInflatedLayout(R.layout.dialog_seekbar);

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
        views.put(requirements.position, seekBarLayout);
    }

    private void createCheckBox(final CheckBoxRequirements requirements) {
        checkWith();

        final String attribute = requirements.attribute;
        final String name = requirements.name;
        final boolean checkedIsTrue = requirements.checkedIsTrue;

        final View checkBoxLayout = getInflatedLayout(R.layout.dialog_checkbox);
        searcher.addFacet(attribute);

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
        views.put(requirements.position, checkBoxLayout);
    }

    private View getInflatedLayout(int layoutId) {
        Activity activity = this.activity != null ? this.activity : getActivity();
        LayoutInflater inflater = activity.getLayoutInflater();
        return inflater.inflate(layoutId, null);
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
        return this;
    }

    private void checkWith() {
        if (searcher == null || activity == null) {
            throw new RuntimeException("You need to prepare the fragment by calling with(activity, searcher) before you can use this method.");
        }
    }

    private class SeekBarRequirements {
        private final String attribute;
        private final String name;
        private Double min;
        private Double max;
        private final int steps;
        private final int position;

        public SeekBarRequirements(String attribute, String name, Double min, Double max, int steps, int position) {
            this.attribute = attribute;
            this.name = name;
            this.min = min;
            this.max = max;
            this.steps = steps;
            this.position = position;
        }

        public void create() {
            if (min == null || max == null) {
                FacetStat stats = searcher.getFacetStat(attribute);
                if (stats == null) {
                    throw new RuntimeException("We need either some FacetStat or given min/max values.");
                }
                min = min == null ? stats.min : min;
                max = max == null ? stats.max : max;
            }
            createSeekBar(this);
        }
    }

    private class CheckBoxRequirements {
        private final String attribute;
        private final String name;
        private final boolean checkedIsTrue;
        private final int position;

        public CheckBoxRequirements(String attribute, String name, boolean checkedIsTrue, int position) {
            this.attribute = attribute;
            this.name = name;
            this.checkedIsTrue = checkedIsTrue;
            this.position = position;
        }

        public void create() {
            createCheckBox(this);
        }
    }
}
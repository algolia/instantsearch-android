package com.algolia.instantsearch.ui.views.filters;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.algolia.instantsearch.R;
import com.algolia.instantsearch.helpers.Searcher;
import com.algolia.instantsearch.model.NumericRefinement;
import com.algolia.instantsearch.model.SearchResults;
import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class NumericSelector extends AppCompatSpinner implements AlgoliaFacetFilter, AdapterView.OnItemSelectedListener {
    public static final Double DEFAULT_VALUE = null;

    private final boolean autoHide;
    private String attributeName;
    private int operator;
    private String defaultLabel;
    private final List<Double> values;

    private Searcher searcher;
    private NumericRefinement currentRefinement;

    // TODO: Let developer customize those layouts
    private final int spinnerItemLayout = android.R.layout.simple_spinner_item;
    private final int spinnerDropdownItemLayout = android.R.layout.simple_spinner_dropdown_item;

    public NumericSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnItemSelectedListener(this);
        final TypedArray filterStyledAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Filter, 0, 0);
        final TypedArray selectorStyleAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.NumericSelector, 0, 0);
        try {
            attributeName = filterStyledAttributes.getString(R.styleable.Filter_attributeName);
            Filters.checkAttributeName(attributeName);
            autoHide = filterStyledAttributes.getBoolean(R.styleable.Filter_autoHide, false);
            operator = selectorStyleAttributes.getInt(R.styleable.NumericSelector_operatorName, NumericRefinement.OPERATOR_EQ);
            defaultLabel = selectorStyleAttributes.getString(R.styleable.NumericSelector_defaultLabel);

            String labelString = selectorStyleAttributes.getString(R.styleable.NumericSelector_labels);
            String valueString = selectorStyleAttributes.getString(R.styleable.NumericSelector_values);

            List<String> labels;
            if (!(labelString == null && valueString == null)) {

                if (labelString == null || valueString == null) {
                    throw new IllegalStateException("You need to either specify both labels and values or none of those.");
                } else {
                    final String[] labelStrings = labelString.split(",");
                    final List<String> valuesStrList = Arrays.asList(valueString.split(","));
                    if (defaultLabel == null) {
                        labels = new ArrayList<>(Arrays.asList(labelStrings));
                        values = new ArrayList<>(valuesStrList.size());
                    } else {
                        labels = new ArrayList<>(labelStrings.length + 1);
                        labels.add(defaultLabel);
                        labels.addAll(Arrays.asList(labelStrings));
                        values = new ArrayList<>(valuesStrList.size() + 1);
                        values.add(DEFAULT_VALUE);
                    }
                    for (String value : valuesStrList) {
                        values.add(Double.parseDouble(value));
                    }

                    if (labels.size() != values.size()) {
                        throw new IllegalStateException("You need to specify as much labels as values ("
                                + labels.size() + " label" + (labels.size() > 1 ? "s" : "") + " but "
                                + values.size() + " value" + (labels.size() > 1 ? "s" : "") + ").");
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, spinnerItemLayout, labels);
                    adapter.setDropDownViewResource(spinnerDropdownItemLayout);
                    setAdapter(adapter);
                }
            } else {
                values = new ArrayList<>();
            }
        } finally {
            filterStyledAttributes.recycle();
            selectorStyleAttributes.recycle();
        }
    }

    @NonNull @Override public String getAttributeName() {
        return attributeName;
    }

    @Override public void onResults(@NonNull SearchResults results, boolean isLoadingMore) {
        Filters.hideIfShouldHide(this, autoHide, results.nbHits == 0);
    }

    @Override public void onError(Query query, AlgoliaException error) {
        Filters.hideIfShouldHide(this, autoHide, true);
    }

    @Override public void initWithSearcher(@NonNull Searcher searcher) {
        this.searcher = searcher;
    }

    @Override public void onReset() {
    }

    @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        final Double selectedValue = values.get(position);
        if (currentRefinement != null) {
            searcher.removeNumericRefinement(currentRefinement);
        }
        if (selectedValue != null) { // don't refine if user selected the default value
            currentRefinement = new NumericRefinement(attributeName, operator, selectedValue);
            searcher.addNumericRefinement(currentRefinement);
        }
        searcher.search(); //TODO: Conditional if refineNow (window) or not (dialog)
    }

    @Override public void onNothingSelected(AdapterView<?> parent) {
    }

    //TODO: Get/Set properties
}

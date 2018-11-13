package com.algolia.instantsearch.ui.views.filters;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;

import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.algolia.instantsearch.R;
import com.algolia.instantsearch.core.events.NumericRefinementEvent;
import com.algolia.instantsearch.core.events.RefinementEvent;
import com.algolia.instantsearch.core.helpers.Searcher;
import com.algolia.instantsearch.core.model.AlgoliaErrorListener;
import com.algolia.instantsearch.core.model.AlgoliaResultsListener;
import com.algolia.instantsearch.core.model.AlgoliaSearcherListener;
import com.algolia.instantsearch.core.model.NumericRefinement;
import com.algolia.instantsearch.core.model.SearchResults;
import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Query;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Displays a dropdown menu to refine a specific attribute with one of several values. */
public class NumericSelector extends AppCompatSpinner implements AlgoliaFilter, AlgoliaResultsListener, AlgoliaErrorListener, AlgoliaSearcherListener, AdapterView.OnItemSelectedListener {
    public static final Double DEFAULT_VALUE = null;

    /** Whether the selector should hide on error or when results are empty. */
    private boolean autoHide;

    /** The name of this NumericSelector's attribute. */
    private String attribute;
    /** The operator to use for refining. */
    private int operator;
    /** The eventual label for a default option that does not refine the attribute. */
    private String defaultLabel;
    /** The List of labels to display. */
    private List<String> labels;
    /** The List of values to refine with. */
    private final List<Double> values;
    /** The currently selected refinement, if any. */
    private NumericRefinement currentRefinement;

    private Searcher searcher;

    // TODO: Let developer customize those layouts
    private final int spinnerItemLayout = android.R.layout.simple_spinner_item;
    private final int spinnerDropdownItemLayout = android.R.layout.simple_spinner_dropdown_item;

    /**
     * Constructs a new NumericSelector with the given context's theme and the supplied attribute set.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     */
    public NumericSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnItemSelectedListener(this);
        final TypedArray viewStyledAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.View, 0, 0);
        final TypedArray widgetStyledAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Widget, 0, 0);
        final TypedArray selectorStyleAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.NumericSelector, 0, 0);
        try {
            attribute = viewStyledAttributes.getString(R.styleable.View_attribute);
            Filters.checkAttributeName(attribute);
            autoHide = widgetStyledAttributes.getBoolean(R.styleable.Widget_autoHide, false);
            operator = selectorStyleAttributes.getInt(R.styleable.NumericSelector_operator, NumericRefinement.OPERATOR_EQ);
            defaultLabel = selectorStyleAttributes.getString(R.styleable.NumericSelector_defaultLabel);

            String labelString = selectorStyleAttributes.getString(R.styleable.NumericSelector_labels);
            String valueString = selectorStyleAttributes.getString(R.styleable.NumericSelector_values);

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
            viewStyledAttributes.recycle();
            widgetStyledAttributes.recycle();
            selectorStyleAttributes.recycle();
        }
    }

    @NonNull public String getAttribute() {
        return attribute;
    }

    @Override public void onResults(@NonNull SearchResults results, boolean isLoadingMore) {
        Filters.hideIfShouldHide(this, autoHide, results.nbHits == 0);
    }

    @Override public void onError(@NonNull Query query, @NonNull AlgoliaException error) {
        Filters.hideIfShouldHide(this, autoHide, true);
    }

    @Override public void initWithSearcher(@NonNull Searcher searcher) {
        this.searcher = searcher;
    }

    @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        final Double selectedValue = values.get(position);
        if (currentRefinement != null) {
            searcher.removeNumericRefinement(currentRefinement);
        }
        if (selectedValue != null) { // don't refine if user selected the default value
            currentRefinement = new NumericRefinement(attribute, operator, selectedValue);
            searcher.addNumericRefinement(currentRefinement);
        }
        searcher.search(); //TODO: Conditional if refineNow (window) or not (dialog)
    }

    @Override public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetachedFromWindow() {
        EventBus.getDefault().unregister(this);
        super.onDetachedFromWindow();
    }

    public boolean isAutoHide() {
        return autoHide;
    }

    public void setAutoHide(boolean autoHide) {
        this.autoHide = autoHide;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
        updateRefinement(attribute, operator);
    }

    public int getOperator() {
        return operator;
    }

    public void setOperator(int operator) {
        this.operator = operator;
        updateRefinement(attribute, operator);
    }

    public void setDefaultLabel(String defaultLabel) {
        this.defaultLabel = defaultLabel;
        labels.remove(0);
        labels.add(0, defaultLabel);
        ((ArrayAdapter<String>) getAdapter()).notifyDataSetChanged();
    }

    //TODO: Add setValues as well, or iterate on API
    public void setLabels(List<String> labels) {
        if (labels.size() != this.labels.size()) {
            throw new IllegalArgumentException("You need to provide " + this.labels.size() + " labels.");
        }
        this.labels = labels;
        ((ArrayAdapter<String>) getAdapter()).notifyDataSetChanged();
    }

    private void updateRefinement(String attribute, int operator) {
        if (currentRefinement != null) {
            searcher.removeNumericRefinement(currentRefinement);
            currentRefinement = new NumericRefinement(attribute, operator, currentRefinement.value);
            searcher.addNumericRefinement(currentRefinement);
            searcher.search();
        }
    }

    @Subscribe
    public void onRefinement(NumericRefinementEvent event) {
        if (event.refinement.attribute.equals(attribute) && event.refinement.operator == operator) {
            if (event.operation == RefinementEvent.Operation.REMOVE) {
                setSelection(0);
            } else {
                for (int i = 0; i < values.size(); i++) {
                    Double valueI = values.get(i);
                    if (event.refinement.value.equals(valueI)) {
                        setSelection(i);
                        break;
                    }
                }
            }
        }
    }
}

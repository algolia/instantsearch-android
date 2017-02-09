package com.algolia.instantsearch.ui.views.filters;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;
import android.widget.CompoundButton;

import com.algolia.instantsearch.R;
import com.algolia.instantsearch.helpers.Searcher;


public class FilterCheckBox extends AppCompatCheckBox implements AlgoliaFacetFilter {
    public final String attribute;
    public final String value;

    public FilterCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray styledAttributesView = context.getTheme().obtainStyledAttributes(attrs, R.styleable.View, 0, 0);
        final TypedArray styledAttributesFilterCheckBox = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FilterCheckBox, 0, 0);
        try {
            attribute = styledAttributesView.getString(R.styleable.View_attribute);
            value = styledAttributesFilterCheckBox.getString(R.styleable.FilterCheckBox_value);
        } finally {
            styledAttributesFilterCheckBox.recycle();
            styledAttributesView.recycle();
        }
    }

    @NonNull @Override public String getAttribute() {
        return attribute;
    }

    @NonNull @Override public void defineListeners(final Searcher searcher) {
        setOnCheckedChangeListener(new FilterCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                searcher.updateFacetRefinement(attribute, value, isChecked)
                        .search();
            }
        });
    }
}
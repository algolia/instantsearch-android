package com.algolia.searchdemo.helper;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.SearchView;

import com.algolia.searchdemo.R;

public class SearchBox extends SearchView {

    public SearchBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SearchBox, 0, 0);

        try {
            final boolean iconifiedByDefault = attrs.getAttributeBooleanValue(android.R.attr.iconifiedByDefault, true);
            final boolean submitButtonEnabled = styledAttributes.getBoolean(R.styleable.SearchBox_submitButtonEnabled, false);
            final boolean autofocus = styledAttributes.getBoolean(R.styleable.SearchBox_autofocus, false);
            Log.e("PLN", "init: " +
                    "submit(" + submitButtonEnabled + "), " +
                    "focus(" + autofocus+ "), " +
                    "iconified(" + iconifiedByDefault + ").");

            if (autofocus) {
                setFocusable(true);
                setIconified(false);
                requestFocusFromTouch();
            }
            setIconifiedByDefault(iconifiedByDefault);
            setSubmitButtonEnabled(submitButtonEnabled);
        } finally {
            styledAttributes.recycle();
        }
    }

}
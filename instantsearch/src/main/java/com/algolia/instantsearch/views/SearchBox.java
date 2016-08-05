package com.algolia.instantsearch.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.SearchView;

import com.algolia.instantsearch.R;

public class SearchBox extends SearchView {

    public SearchBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }

        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SearchBox, 0, 0);

        try {
            final boolean iconifiedByDefault;
            iconifiedByDefault = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB &&
                    attrs.getAttributeBooleanValue(android.R.attr.iconifiedByDefault, true); // iconifiedByDefault requires API >= 11
            final boolean submitButtonEnabled = styledAttributes.getBoolean(R.styleable.SearchBox_submitButtonEnabled, false);
            final boolean autofocus = styledAttributes.getBoolean(R.styleable.SearchBox_autofocus, false);

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
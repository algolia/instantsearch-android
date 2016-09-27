package com.algolia.instantsearch.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import com.algolia.instantsearch.R;

public class SearchBox extends SearchView {

    public SearchBox(@NonNull Context context, @NonNull AttributeSet attrs) {
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

    /**
     * Disable fullscreen keyboard display in landscape mode. This only works on >= 4.1 devices.
     */
    public void disableFullScreen() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            setImeOptions(getImeOptions() | EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        }
    }

    /**
     * Enable fullscreen keyboard display in landscape mode. This only works on >= 4.1 devices.
     */
    public void enableFullScreen() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            setImeOptions(getImeOptions() & EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        }
    }

}
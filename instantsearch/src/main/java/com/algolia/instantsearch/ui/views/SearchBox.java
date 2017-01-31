package com.algolia.instantsearch.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;

import com.algolia.instantsearch.R;

public class SearchBox extends SearchView {

    public SearchBox(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }

        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SearchBox, 0, 0);

        try {
            setIconifiedByDefault(false); // By default, don't collapse SearchBox
            for (int i = 0; i < attrs.getAttributeCount(); i++) {
                if ("iconifiedByDefault".equals(attrs.getAttributeName(i))) {
                    setIconifiedByDefault(attrs.getAttributeBooleanValue(i, false)); // Unless iconifiedByDefault is set
                }
            }

            if (styledAttributes.getBoolean(R.styleable.SearchBox_autofocus, false)) {
                setFocusable(true);
                setIconified(false);
                requestFocusFromTouch();
            }
            setSubmitButtonEnabled(styledAttributes.getBoolean(R.styleable.SearchBox_submitButtonEnabled, false));
        } finally {
            styledAttributes.recycle();
        }
    }

    /**
     * Disable fullscreen keyboard display in landscape mode. This only works on {@literal >= 4.1} devices.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public void disableFullScreen() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            setImeOptions(getImeOptions() | EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        }
    }

    /**
     * Enable fullscreen keyboard display in landscape mode. This only works on {@literal >= 4.1} devices.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public void enableFullScreen() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            setImeOptions(getImeOptions() & EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        }
    }

}
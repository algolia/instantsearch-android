package com.algolia.instantsearch.ui.views.filters;

import android.view.View;

import com.algolia.instantsearch.model.Errors;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

class Filters {
    static void hideIfShouldHide(View view, Boolean autoHide, Boolean shouldHide) {
        if (autoHide) {
            view.setVisibility(shouldHide ? GONE : VISIBLE);
        }
    }

    static void checkAttributeName(String attributeName) {
        if (attributeName == null) {
            throw new IllegalStateException(Errors.FILTER_MISSING_ATTRIBUTE);
        }
    }
}

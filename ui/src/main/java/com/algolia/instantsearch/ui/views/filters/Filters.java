package com.algolia.instantsearch.ui.views.filters;

import android.view.View;

import com.algolia.instantsearch.core.model.Errors;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
/** Hosts helper functions common to several {@code com.algolia.instantsearch.ui.views.filters}.*/
abstract class Filters {
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

package com.algolia.instantsearch.model;

/**
 * Contains the error message for every potential runtime errors.
 */
public class Errors {
    // region Core
    public static final String SEARCHER_GET_BEFORE_CREATE = "You cannot use Searcher#get before having called Searcher#create at least once.";
    // endregion

    // region UI
    public static final String ADAPTER_UNKNOWN_VIEW = "Unrecognized view class (%s): Your view should either use/extend a system view or implement AlgoliaHitView.";

    public static final String BINDING_NO_ATTRIBUTE = "Your view lacks an algolia:attribute.";
    public static final String BINDING_VIEW_NO_ID = "Your View for attribute %s must have an android:id.";

    public static final String FILTER_MISSING_ATTRIBUTE = "You must specify an attribute";

    public static final String HITS_INFINITESCROLL_BUT_REMAINING = "You specified infiniteScroll=\"false\" and remainingItemsBeforeLoading, but they are mutually exclusive.";

    public static final String LAYOUT_MISSING_RESULT_LISTENER = "You need to add at least one AlgoliaResultsListener.";
    public static final String LAYOUT_MISSING_HITS_ITEMLAYOUT = "To use the Hits widget, you need to specify an item layout with algolia:itemLayout.";
    public static final String LAYOUT_MISSING_SEARCHBOX = "No SearchBox or SearchView was found in your activity.";
    public static final String LAYOUT_MISSING_ALGOLIAFILTER = "No AlgoliaFilter was found in view %s.";
    public static final String LAYOUT_TOO_MANY_SEARCHVIEWS = "If you have several SearchViews, you need to identify the Algolia one with @id/searchBox.";
    public static final String LAYOUT_TOO_MANY_SEARCHBOXES = "You cannot have more than one SearchBox.";

    public static final String PROGRESS_WITHOUT_SEARCHPLATE = "Could not find a view with id @id/search_plate in SearchView.";

    public static final String REFINEMENTS_MISSING_ATTRIBUTE = "You need to specify the attribute to refine on with algolia:attribute.";
    public static final String REFINEMENTS_MISSING_ITEM = "RefinementList has no item at position %d.";

    public static final String SORT_INVALID_VALUE = "invalid sortBy value: %s is none of \"isRefined\"|\"count:asc\"|\"count:desc\"|\"name:asc\"|\"name:desc\".";
    public static final String SORT_INVALID_ARRAY = "invalid sortBy value: %s is not a valid JSONArray.";

    public static final String TOGGLE_MISSING_VALUEON = "Your TwoValuesToggle should have a valueOn attribute.";
    public static final String TOGGLE_MISSING_VALUEOFF = "Your TwoValuesToggle should have a valueOff attribute.";
    // endregion
}

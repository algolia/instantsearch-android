package com.algolia.instantsearch.model;

public class Errors {
    public static final String ADAPTER_UNKNOWN_VIEW = "Unrecognized view class (%s): Your view should either use/extend a system view or implement AlgoliaHitView.";

    public static final String BINDING_HIGHLIGHTED_NO_ATTR = "You need an algolia:attribute to use algolia:highlighted.";
    public static final String BINDING_HIGHLIGHTING_NO_ATTR = "You need an algolia:attribute to use algolia:highlightingColor.";
    public static final String BINDING_NO_ATTR = "You need an algolia:attribute to use algolia:highlighted and algolia:highlighting.";
    public static final String BINDING_COLOR_INVALID = "algolia:highlightingColor should be an @android:color or @color resource.";
    public static final String BINDING_VIEW_NO_ID = "Your View for attribute %s is missing an android:id.";

    public static final String FILTER_MISSING_ATTRIBUTE = "You must specify an attributeName";

    public static final String HITS_NO_ITEMLAYOUT = "Your Hits widget is missing an algolia:itemLayout attribute to specify the hits item layout.";
    public static final String HITS_INFINITESCROLL = "You specified infiniteScroll=\"false\" and remainingItemsBeforeLoading, but they are mutually exclusive.";

    public static final String LAYOUT_MISSING_HITS = "You need to add at least one AlgoliaResultsListener.";
    public static final String LAYOUT_MISSING_HITS_ITEMLAYOUT = "To use the Hits widget, you need to specify an item layout with algolia:itemLayout.";
    public static final String LAYOUT_MISSING_SEARCHBOX = "No SearchBox or SearchView was found in your activity.";
    public static final String LAYOUT_TOO_MANY_SEARCHVIEWS = "If you have several SearchViews, you need to identify the Algolia one with @id/searchBox.";
    public static final String LAYOUT_TOO_MANY_SEARCHBOXES = "You cannot have more than one SearchBox.";

    public static final String REFINEMENTS_MISSING_ATTRIBUTE = "You need to specify the attribute to refine on with algolia:attribute.";
    public static final String REFINEMENTS_MISSING_ITEM = "RefinementList has no item at position %d.";

    public static final String SORT_INVALID_VALUE = "invalid sortBy value: %s is none of \"isRefined\"|\"count:asc\"|\"count:desc\"|\"name:asc\"|\"name:desc\".";
    public static final String SORT_INVALID_ARRAY = "invalid sortBy value: %s is not a valid JSONArray.";

    public static final String PROGRESS_WITHOUT_SEARCHPLATE = "Could not find a view with id @id/search_plate in SearchView.";
}

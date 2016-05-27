package com.algolia.instantsearch.model;

public class Errors {
    public static final String BINDING_HIGHLIGHTED_NO_ATTR = "You need an algolia:attribute to use algolia:highlighted.";
    public static final String BINDING_HIGHLIGHTING_NO_ATTR = "You need an algolia:attribute to use algolia:highlighting.";
    public static final String BINDING_NO_ATTR = "You need an algolia:attribute to use algolia:highlighted and algolia:highlighting.";
    public static final String BINDING_COLOR_INVALID = "algolia:highlightingColor should be an @android:color or @color resource.";

    public static final String HITS_INFINITESCROLL = "You specified both disableInfiniteScroll and remainingItemsBeforeLoading, but they are mutually exclusive.";

    public static final String IMAGELOAD_MISSING_URL = "There should only be one url per image.";

    public static final String LAYOUT_MISSING_EMPTY = "You need to add an empty view identified as @id/empty.";
    public static final String LAYOUT_MISSING_HITS = "You need to add Hits identified as @id/hits.";
    public static final String LAYOUT_MISSING_SEARCHBOX = "You need to add a searchBox identified as @id/searchBox.";

    public static final String LOADMORE_FAIL = "Error while loading more data.";

    public static final String ADAPTER_UNKNOWN_VIEW = "Unrecognized view class ({className}): Your view should either use/extend a system view or implement AlgoliaAttributeView.";
}

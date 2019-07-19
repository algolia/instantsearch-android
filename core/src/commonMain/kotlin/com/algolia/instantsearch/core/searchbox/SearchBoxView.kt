package com.algolia.instantsearch.core.searchbox

import com.algolia.instantsearch.core.event.Event


public interface SearchBoxView {

    public var onQueryChanged: Event<String?>
    public var onQuerySubmitted: Event<String?>

    public fun setText(text: String?)
}
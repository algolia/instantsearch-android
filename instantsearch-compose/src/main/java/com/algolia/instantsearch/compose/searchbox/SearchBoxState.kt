package com.algolia.instantsearch.compose.searchbox

import com.algolia.instantsearch.compose.searchbox.internal.SearchBoxStateImpl
import com.algolia.instantsearch.core.searchbox.SearchBoxView

/**
 * Search box query component for compose.
 *
 * Example using [SearchBoxState] with material's TextField:
 *
 * ```
 * @Composable
 * public fun SearchBox(
 *     searchBoxState: SearchBoxState
 * ) {
 *     TextField(
 *         // set query as text value
 *         value = searchBoxState.query,
 *         // update text on value change
 *         onValueChange = { searchBoxState.setText(it) },
 *
 *         // set ime action to "search"
 *         keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
 *         // set text as query submit on search action
 *         keyboardActions = KeyboardActions(
 *             onSearch = { searchBoxState.setText(searchBoxState.query, true)}
 *         )
 *     )
 * }
 * ```
 */
public interface SearchBoxState : SearchBoxView {

    /**
     * Search box query.
     */
    public val query: String
}

/**
 * Creates an instance of [SearchBoxState].
 *
 * @param query default query value
 */
public fun SearchBoxState(query: String = ""): SearchBoxState {
    return SearchBoxStateImpl(query)
}

package com.algolia.instantsearch.compose.searchbox

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.compose.R
import com.algolia.instantsearch.compose.searchbox.internal.SearchClearIcon
import com.algolia.instantsearch.compose.searchbox.internal.searchColors

/**
 * Search Box compose component.
 *
 * @param modifier Modifier to be applied
 * @param textStyle the style to be applied to the input text
 * @param searchQuery search box query component
 * @param onValueChange callback triggered on each text update
 * @param colors will be used to resolve color of the text, content and background
 * @param placeHolderText the placeholder to be displayed when the the input text is empty
 * @param elevation controls the size of the shadow below the surface
 */
@Composable
public fun SearchBox(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
    onValueChange: (String, Boolean) -> Unit = { _, _ -> },
    searchQuery: SearchQuery = SearchQuery(),
    colors: TextFieldColors = searchColors(),
    placeHolderText: String = stringResource(R.string.search_box_hint),
    elevation: Dp = 1.dp,
) {
    Card(modifier = modifier, elevation = elevation) {
        TextField(
            value = searchQuery.query,
            textStyle = textStyle.merge(TextStyle(textDecoration = TextDecoration.None)),
            onValueChange = {
                searchQuery.query = it
                searchQuery.valueChanged(it, false)
                onValueChange(it, false)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onBackground
                )
            },
            placeholder = {
                Text(
                    text = placeHolderText,
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.2f)
                )
            },
            trailingIcon = {
                val visible = searchQuery.query.isNotEmpty()
                SearchClearIcon(visible) {
                    searchQuery.query = ""
                    searchQuery.valueChanged("", false)
                    onValueChange("", false)
                }
            },
            singleLine = true,
            colors = colors,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    searchQuery.valueChanged(searchQuery.query, true)
                    onValueChange(searchQuery.query, true)
                }
            )
        )
    }
}

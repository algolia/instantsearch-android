package com.algolia.instantsearch.compose.searchbox

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.compose.R
import com.algolia.instantsearch.compose.searchbox.internal.SearchClearIcon
import com.algolia.instantsearch.compose.searchbox.internal.SearchColors

/**
 * Implementation of Search Box.
 *
 * @param modifier Modifier to be applied
 * @param textStyle the style to be applied to the input text
 * @param onValueChange the callback that is triggered when each text update
 * @param query the text shown in the text field
 * @param colors will be used to resolve color of the text, content and background
 */
@Composable
public fun SearchBox(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
    onValueChange: (String, Boolean) -> Unit = { _, _ -> },
    query: MutableState<String> = mutableStateOf(""),
    colors: TextFieldColors = SearchColors()
) {
    val text = rememberSaveable { query }
    Card(modifier = modifier, elevation = 4.dp) {
        TextField(
            value = text.value,
            textStyle = textStyle.merge(TextStyle(textDecoration = TextDecoration.None)),
            onValueChange = {
                val isSubmit = it.endsWith("\n")
                val value = if (isSubmit) it.removeSuffix("\n") else it
                text.value = value
                onValueChange(value, isSubmit)
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
                    text = stringResource(R.string.search_box_hint),
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.2f)
                )
            },
            trailingIcon = {
                val visible = query.value.isNotEmpty()
                SearchClearIcon(visible) {
                    text.value = ""
                    onValueChange("", false)
                }
            },
            maxLines = 1,
            colors = colors,
        )
    }
}

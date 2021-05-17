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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
    colors: TextFieldColors = searchColors(),
    placeHolderText: String = stringResource(R.string.search_box_hint),
    elevation: Dp = 1.dp,
) {
    val text = rememberSaveable { query }
    Card(modifier = modifier, elevation = elevation) {
        TextField(
            value = text.value,
            textStyle = textStyle.merge(TextStyle(textDecoration = TextDecoration.None)),
            onValueChange = {
                text.value = it
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
                val visible = query.value.isNotEmpty()
                SearchClearIcon(visible) {
                    text.value = ""
                    onValueChange("", false)
                }
            },
            singleLine = true,
            colors = colors,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onValueChange(text.value, true)
                }
            )
        )
    }
}

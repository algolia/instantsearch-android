package com.algolia.instantsearch.compose.searchbox

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.compose.R
import com.algolia.instantsearch.compose.searchbox.internal.DefaultLeadingIcon
import com.algolia.instantsearch.compose.searchbox.internal.DefaultTrailingIcon

/**
 * Search Box compose component.
 *
 * @param modifier Modifier to be applied
 * @param textStyle the style to be applied to the input text
 * @param searchBoxState search box query component
 * @param onValueChange callback triggered on each text update
 * @param colors will be used to resolve color of the text, content and background
 * @param placeHolderText the placeholder to be displayed when the the input text is empty
 * @param elevation controls the size of the shadow below the surface
 */
@Deprecated("implement your search box manually instead")
@Composable
public fun SearchBox(
    modifier: Modifier = Modifier,
    searchBoxState: SearchBoxState = SearchBoxState(),
    onValueChange: ((String, Boolean) -> Unit)? = null,
    enabled: Boolean = true,
    textStyle: TextStyle = LocalTextStyle.current,
    placeHolderText: String = stringResource(R.string.alg_is_compose_search_box_hint),
    colors: TextFieldColors = defaultSearchBoxColors(),
    leadingIcon: @Composable (() -> Unit)? = { DefaultLeadingIcon() },
    trailingIcon: @Composable (() -> Unit)? = { DefaultTrailingIcon(searchBoxState) },
    elevation: Dp = 1.dp,
) {
    Surface(modifier = modifier, elevation = elevation) {
        TextField(
            value = searchBoxState.query,
            textStyle = textStyle.merge(TextStyle(textDecoration = TextDecoration.None)),
            onValueChange = {
                searchBoxState.setText(it)
                onValueChange?.invoke(it, false)
            },
            leadingIcon = leadingIcon,
            placeholder = {
                Text(text = placeHolderText)
            },
            trailingIcon = trailingIcon,
            singleLine = true,
            enabled = enabled,
            colors = colors,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    searchBoxState.setText(searchBoxState.query, true)
                    onValueChange?.invoke(searchBoxState.query, true)
                }
            )
        )
    }
}

@Composable
public fun defaultSearchBoxColors(
    textColor: Color = LocalContentColor.current.copy(LocalContentAlpha.current),
    backgroundColor: Color = MaterialTheme.colors.surface,
    onBackgroundColor: Color = MaterialTheme.colors.onSurface,
): TextFieldColors {
    return TextFieldDefaults.textFieldColors(
        textColor = textColor,
        backgroundColor = backgroundColor,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        leadingIconColor = onBackgroundColor,
        placeholderColor = onBackgroundColor.copy(alpha = 0.2f),
    )
}

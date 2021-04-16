package com.algolia.instantsearch.compose.searchbox

import androidx.compose.animation.*
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import com.algolia.instantsearch.compose.R

@Composable
public fun SearchBox(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
    onValueChange: (String, Boolean) -> Unit = { _, _ -> },
    query: MutableState<String> = mutableStateOf("")
) {
    val text = rememberSaveable { query }
    Card(modifier = modifier) {
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
            colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.background)
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun SearchClearIcon(
    visible: Boolean,
    onClick: () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(
            initialOffsetX = { fullWidth -> fullWidth / 3 },
            animationSpec = tween(100, easing = LinearOutSlowInEasing)
        ) + fadeIn(),
        exit = slideOutHorizontally(
            targetOffsetX = { fullWidth -> fullWidth / 3 },
            animationSpec = tween(100, easing = LinearOutSlowInEasing)
        ) + fadeOut()
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = null,
            tint = MaterialTheme.colors.onBackground,
            modifier = Modifier.clickable(onClick = onClick)
        )
    }
}

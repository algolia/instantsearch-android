package com.algolia.instantsearch.compose.searchbox.internal

import androidx.compose.animation.*
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.algolia.instantsearch.compose.searchbox.SearchQuery

/**
 * Search Box default colors.
 */
@Composable
internal fun defaultSearchColors(): TextFieldColors {
    return TextFieldDefaults.textFieldColors(
        backgroundColor = MaterialTheme.colors.surface,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        leadingIconColor = MaterialTheme.colors.onSurface,
        placeholderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
    )
}

@Composable
internal fun SearchIcon() {
    Icon(
        imageVector = Icons.Filled.Search,
        contentDescription = null,
    )
}

@Composable
internal fun SearchClearIcon(searchQuery: SearchQuery, onValueChange: (String, Boolean) -> Unit) {
    val visible = searchQuery.query.isNotEmpty()
    SearchClearIcon(visible) {
        searchQuery.query = ""
        searchQuery.changeValue("", false)
        onValueChange("", false)
    }
}

/**
 * Search box default clear icon.
 */
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
            modifier = Modifier.clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ),
        )
    }
}

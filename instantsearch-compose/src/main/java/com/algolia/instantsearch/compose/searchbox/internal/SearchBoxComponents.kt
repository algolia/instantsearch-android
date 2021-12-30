package com.algolia.instantsearch.compose.searchbox.internal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.algolia.instantsearch.compose.searchbox.SearchBoxState

@Composable
internal fun DefaultLeadingIcon() {
    Icon(
        imageVector = Icons.Filled.Search,
        contentDescription = null,
    )
}

@Composable
internal fun DefaultTrailingIcon(searchBoxState: SearchBoxState) {
    if (searchBoxState.query.isNotEmpty()) {
        Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = null,
            tint = MaterialTheme.colors.onBackground,
            modifier = Modifier.clickable(
                onClick = { searchBoxState.setText("") },
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            )
        )
    }
}

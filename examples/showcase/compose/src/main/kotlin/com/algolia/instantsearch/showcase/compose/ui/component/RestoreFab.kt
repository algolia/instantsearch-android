package com.algolia.instantsearch.showcase.compose.ui.component

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restore
import androidx.compose.runtime.Composable

@Composable
fun RestoreFab(onClick: () -> Unit = {}) {
    FloatingActionButton(
        backgroundColor = MaterialTheme.colors.background,
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Default.Restore,
            contentDescription = "restore"
        )
    }
}

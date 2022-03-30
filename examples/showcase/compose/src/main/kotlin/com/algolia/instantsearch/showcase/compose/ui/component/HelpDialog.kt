package com.algolia.instantsearch.showcase.compose.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.showcase.compose.R

@Composable
fun HelpDialog(openDialog: MutableState<Boolean>, text: String) {
    HelpDialog(openDialog, arrayOf(text))
}

@Composable
fun HelpDialog(openDialog: MutableState<Boolean>, text: Array<String>) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = stringResource(R.string.help))
            },
            text = {
                Column {
                    text.forEach {
                        Text(text = it, modifier = Modifier.padding(vertical = 2.dp))
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { openDialog.value = false }) {
                    Text(stringResource(R.string.ok))
                }
            }
        )
    }
}

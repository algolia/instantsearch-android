package com.algolia.instantsearch.examples.android.showcase.compose.filter.list

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.compose.filter.list.FilterListState
import com.algolia.instantsearch.examples.android.showcase.compose.showcaseTitle
import com.algolia.instantsearch.examples.android.showcase.compose.ui.component.FilterList
import com.algolia.instantsearch.examples.android.showcase.compose.ui.component.HeaderFilter
import com.algolia.instantsearch.examples.android.showcase.compose.ui.component.HeaderFilterConnector
import com.algolia.instantsearch.examples.android.showcase.compose.ui.component.TitleTopBar
import com.algolia.instantsearch.filter.Filter

@Composable
fun <T : Filter> Activity.FilterListScreen(
    title: String = showcaseTitle,
    filterHeader: HeaderFilterConnector,
    filterListState: FilterListState<T>
) {
    Scaffold(
        topBar = {
            TitleTopBar(title = title)
        },
        content = { padding ->
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(padding)) {
                HeaderFilter(
                    modifier = Modifier.padding(16.dp),
                    filterHeader = filterHeader
                )
                Row(Modifier.padding(horizontal = 16.dp)) {
                    FilterList(
                        modifier = Modifier.weight(0.5f),
                        filterListState = filterListState
                    )
                    Spacer(modifier = Modifier.weight(0.5f))
                }
            }
        }
    )
}

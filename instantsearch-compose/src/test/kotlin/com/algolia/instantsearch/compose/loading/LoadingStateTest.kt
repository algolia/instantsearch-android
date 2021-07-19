package com.algolia.instantsearch.compose.loading

import androidx.compose.runtime.snapshots.Snapshot
import kotlin.test.Test
import kotlin.test.assertEquals

public class LoadingStateTest {

    @Test
    public fun testLoading() {
        val loadingState = LoadingState(false)
        Snapshot.takeSnapshot {
            loadingState.onReload = { loadingState.setIsLoading(true) }
            loadingState.reload()
            assertEquals(true, loadingState.loading)
        }
        assertEquals(false, loadingState.loading)
    }
}

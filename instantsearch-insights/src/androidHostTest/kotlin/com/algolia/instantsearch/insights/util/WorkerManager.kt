package com.algolia.instantsearch.insights.util

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.work.Configuration
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper

internal fun setupWorkManager() {
    val context = ApplicationProvider.getApplicationContext<Application>()
    val config = Configuration.Builder()
        .setExecutor(SynchronousExecutor())
        .build()

    WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
}

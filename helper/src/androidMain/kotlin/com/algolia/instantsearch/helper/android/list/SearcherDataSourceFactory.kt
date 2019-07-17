package com.algolia.instantsearch.helper.android.list

import androidx.paging.DataSource


abstract class SearcherDataSourceFactory<T> : DataSource.Factory<Int, T>() {

    public lateinit var lastDataSource: DataSource<Int, T>

    /**
     * Creates a DataSource that will be returned when [create] is called.
     */
    abstract fun createDataSource(): DataSource<Int, T>

    override fun create(): DataSource<Int, T> {
        lastDataSource = createDataSource()
        return lastDataSource
    }
}
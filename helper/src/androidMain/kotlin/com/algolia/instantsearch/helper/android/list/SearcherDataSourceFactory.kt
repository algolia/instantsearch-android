package com.algolia.instantsearch.helper.android.list

import androidx.paging.DataSource


/**
 * A DataSourceFactory that keeps track of the latest DataSource created,
 * to invalidate it when a [connected filter state][connectFilterState] changes.
 */
abstract class SearcherDataSourceFactory<T> : DataSource.Factory<Int, T>() {

    /**
     * The last DataSource created by this Factory.
     */
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
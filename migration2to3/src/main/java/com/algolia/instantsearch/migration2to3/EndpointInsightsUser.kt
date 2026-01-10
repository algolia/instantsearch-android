package com.algolia.instantsearch.migration2to3

import io.ktor.client.statement.HttpResponse

/**
 * [Documentation][https://www.algolia.com/doc/api-client/methods/insights/?language=kotlin]
 */
public interface EndpointInsightsUser {

    /**
     * Send a [InsightsEvent.View] to capture the [filters] a user uses when viewing.
     *
     * @param indexName Name of the index related to the view.
     * @param eventName Name of the event.
     * @param filters The [Filter.Facet] to capture.
     * @param timestamp An optional timestamp for the time of the event.
     * The server will automatically assign a timestamp if no value is passed
     */
    public suspend fun viewedFilters(
        indexName: IndexName,
        eventName: EventName,
        filters: List<Filter.Facet>,
        timestamp: Long? = null
    ): HttpResponse

    /**
     * Send a [InsightsEvent.View] to capture clicked items.
     *
     * @param indexName Name of the index related to the view.
     * @param eventName Name of the event.
     * @param objectIDs The [ObjectID] to capture.
     * @param timestamp An optional timestamp for the time of the event.
     * The server will automatically assign a timestamp if no value is passed
     */
    public suspend fun viewedObjectIDs(
        indexName: IndexName,
        eventName: EventName,
        objectIDs: List<ObjectID>,
        timestamp: Long? = null
    ): HttpResponse

    /**
     * Send a [InsightsEvent.Click] to capture the filters a user clicks on.
     *
     * @param indexName Name of the index related to the view.
     * @param eventName Name of the event.
     * @param filters The [Filter.Facet] to capture.
     * @param timestamp An optional timestamp for the time of the event.
     * The server will automatically assign a timestamp if no value is passed
     */
    public suspend fun clickedFilters(
        indexName: IndexName,
        eventName: EventName,
        filters: List<Filter.Facet>,
        timestamp: Long? = null
    ): HttpResponse

    /**
     * Send a [InsightsEvent.Click] to capture clicked items.
     *
     * @param indexName Name of the index related to the view.
     * @param eventName Name of the event.
     * @param objectIDs The [ObjectID] to capture.
     * @param timestamp An optional timestamp for the time of the event.
     * The server will automatically assign a timestamp if no value is passed
     */
    public suspend fun clickedObjectIDs(
        indexName: IndexName,
        eventName: EventName,
        objectIDs: List<ObjectID>,
        timestamp: Long? = null
    ): HttpResponse

    /**
     * Send a [InsightsEvent.Click] to capture a query and its clicked items and positions.
     *
     * @param indexName Name of the index related to the view.
     * @param eventName Name of the event.
     * @param objectIDs The [ObjectID] to capture.
     * @param positions Position of the click in the list of Algolia search results.
     * @param timestamp An optional timestamp for the time of the event.
     * The server will automatically assign a timestamp if no value is passed
     */
    public suspend fun clickedObjectIDsAfterSearch(
        indexName: IndexName,
        eventName: EventName,
        queryID: QueryID,
        objectIDs: List<ObjectID>,
        positions: List<Int>,
        timestamp: Long? = null
    ): HttpResponse

    /**
     * Send a [InsightsEvent.Conversion] to capture the filters a user uses when converting.
     *
     * @param indexName Name of the index related to the view.
     * @param eventName Name of the event.
     * @param filters The [Filter.Facet] to capture.
     * @param timestamp An optional timestamp for the time of the event.
     * The server will automatically assign a timestamp if no value is passed
     */
    public suspend fun convertedFilters(
        indexName: IndexName,
        eventName: EventName,
        filters: List<Filter.Facet>,
        timestamp: Long? = null
    ): HttpResponse

    /**
     * Send a [InsightsEvent.Conversion] to capture clicked items.
     *
     * @param indexName Name of the index related to the view.
     * @param eventName Name of the event.
     * @param objectIDs The [ObjectID] to capture.
     * @param timestamp An optional timestamp for the time of the event.
     * The server will automatically assign a timestamp if no value is passed
     */
    public suspend fun convertedObjectIDs(
        indexName: IndexName,
        eventName: EventName,
        objectIDs: List<ObjectID>,
        timestamp: Long? = null
    ): HttpResponse

    /**
     * Send a [InsightsEvent.Conversion] to capture a query and its clicked items.
     *
     * @param indexName Name of the index related to the view.
     * @param eventName Name of the event.
     * @param queryID The [ResponseSearch.queryID] [QueryID]
     * @param objectIDs The [ObjectID] to capture.
     * @param timestamp An optional timestamp for the time of the event.
     * The server will automatically assign a timestamp if no value is passed
     */
    public suspend fun convertedObjectIDsAfterSearch(
        indexName: IndexName,
        eventName: EventName,
        queryID: QueryID,
        objectIDs: List<ObjectID>,
        timestamp: Long? = null
    ): HttpResponse
}


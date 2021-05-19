package com.algolia.instantsearch.insights.exception

/**
 * InstantSearch Insights exceptions.
 */
public sealed class InsightsException(override val message: String? = null) : Exception(message) {

    /**
     * Will be thrown when you try to access an index through the Insights.shared
     * method without having registered the index through the Insights.register method first.
     */
    public class IndexNotRegistered : InsightsException("You need to call Insights.register before Insights.shared")

    /**
     * Will be thrown when you call Insights.viewed without Insights.userToken first.
     */
    public class NoUserToken : InsightsException("You need to set Insights.userToken first.")
    // TODO: Remove exception once default userToken
}

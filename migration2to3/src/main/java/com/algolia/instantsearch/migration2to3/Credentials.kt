package com.algolia.instantsearch.migration2to3



public interface Credentials {

    /**
     * [ApplicationID] to target. Is passed as a HTTP header.
     */
    public val applicationID: String

    /**
     * ApiKey for a given ApplicationId. Is passed as a HTTP header.
     * To maintain security, never use your Admin [APIKey] on your front end or share it with anyone.
     * In your front end, use the Search-only [APIKey] or any other key that has search-only rights.
     */
    public val apiKey: String
}

package com.algolia.instantsearch.migration2to3

/**
 * Client to manage [InsightsEvent].
 */
public interface ClientInsights : EndpointInsights, Configuration, Credentials, CustomRequester {

    /**
     * Create a [User] instance.
     *
     * @param userToken user token.
     */
    public fun User(userToken: UserToken): User

    /**
     * Represents an Insights User.
     *
     * @param insights insights endpoint
     * @param userToken user token
     */
    public class User(
        public val insights: EndpointInsights,
        public val userToken: UserToken,
    ) : EndpointInsightsUser by EndpointInsightsUser(insights, userToken)

    public companion object
}

/**
 * Create a [ClientInsights] instance.
 *
 * @param applicationID application ID
 * @param apiKey API Key
 */
public fun ClientInsights(
    applicationID: String,
    apiKey: String,
): ClientInsights = ClientInsightsImpl(
    Transport(
        ConfigurationInsights(applicationID, apiKey),
        Credentials(applicationID, apiKey)
    )
)

/**
 * Create a [ClientSearch] instance.
 *
 * @param configuration insights configuration
 */
public fun ClientInsights(
    configuration: ConfigurationInsights,
): ClientInsights = ClientInsightsImpl(Transport(configuration, configuration))

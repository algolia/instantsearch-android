package com.algolia.instantsearch.migration2to3

internal class ClientInsightsImpl internal constructor(
    internal val transport: Transport,
) : ClientInsights,
    EndpointInsights by EndpointInsights(transport),
    Configuration by transport,
    Credentials by transport.credentials,
    CustomRequester by transport {

    override fun User(userToken: UserToken): ClientInsights.User {
        return ClientInsights.User(insights = this, userToken = userToken)
    }
}

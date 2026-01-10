package com.algolia.instantsearch.migration2to3

import com.algolia.client.model.querysuggestions.LogLevel
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.SIMPLE

public enum class Compression {
    None,
    Gzip
}

internal const val DEFAULT_WRITE_TIMEOUT: Long = 30000L
internal const val DEFAULT_READ_TIMEOUT: Long = 5000L
internal const val DEFAULT_CONNECT_TIMEOUT: Long = 2000L
internal val DEFAULT_LOG_LEVEL = LogLevel.SKIP

public interface ConfigurationInsights : Configuration, Credentials

/**
 * Create a [ConfigurationInsights] instance.
 *
 * @param applicationID application ID
 * @param apiKey API key
 * @param writeTimeout write timout
 * @param readTimeout read timeout
 * @param logLevel logging level
 * @param hosts insights region hosts
 * @param defaultHeaders default headers
 * @param engine http client engine
 * @param httpClientConfig http client configuration
 */
public fun ConfigurationInsights(
    applicationID: ApplicationID,
    apiKey: APIKey,
    writeTimeout: Long = DEFAULT_WRITE_TIMEOUT,
    readTimeout: Long = DEFAULT_READ_TIMEOUT,
    logLevel: LogLevel = DEFAULT_LOG_LEVEL,
    hosts: List<RetryableHost> = insightHosts,
    defaultHeaders: Map<String, String>? = null,
    engine: HttpClientEngine? = null,
    httpClientConfig: (HttpClientConfig<*>.() -> Unit)? = null,
    logger: io.ktor.client.plugins.logging.Logger = Logger.SIMPLE,
    connectTimeout: Long = DEFAULT_CONNECT_TIMEOUT,
): ConfigurationInsights = ConfigurationInsightsImpl(
    applicationID = applicationID,
    apiKey = apiKey,
    writeTimeout = writeTimeout,
    readTimeout = readTimeout,
    logLevel = logLevel,
    hosts = hosts,
    defaultHeaders = defaultHeaders,
    engine = engine,
    httpClientConfig = httpClientConfig,
    logger = logger,
    connectTimeout = connectTimeout,
)

internal data class ConfigurationInsightsImpl(
    override val applicationID: ApplicationID,
    override val apiKey: APIKey,
    override val writeTimeout: Long,
    override val readTimeout: Long,
    override val logLevel: LogLevel,
    override val hosts: List<RetryableHost>,
    override val defaultHeaders: Map<String, String>?,
    override val engine: HttpClientEngine?,
    override val httpClientConfig: (HttpClientConfig<*>.() -> Unit)?,
    override val logger: Logger,
    override val connectTimeout: Long,
) : ConfigurationInsights {

    override val compression: Compression = Compression.None
    override val httpClient: HttpClient = getHttpClient()
}

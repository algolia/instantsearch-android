package com.algolia.instantsearch.migration2to3

import com.algolia.client.exception.AlgoliaApiException
import com.algolia.client.exception.AlgoliaClientException
import com.algolia.client.exception.AlgoliaRetryException
import io.ktor.client.call.HttpClientCall
import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.timeout
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.util.reflect.TypeInfo
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock







//internal class Transport(
//    configuration: Configuration,
//    private val credentialsOrNull: Credentials?,
//) : CustomRequester, Configuration by configuration {
//
//    private val hostStatusExpirationDelayMS: Long = 1000L * 60L * 5L
//    private val mutex: Mutex = Mutex()
//
//    internal val credentials get() = requireNotNull(credentialsOrNull)
//
//    /**
//     * Runs an HTTP request (with retry strategy) and get a result as [T].
//     *
//     * @param httpMethod http method (verb)
//     * @param callType indicate whether the HTTP call performed is of type Read or Write
//     * @param path request path
//     * @param requestOptions additional request configuration
//     * @param body request body
//     */
//    internal suspend inline fun <reified T> request(
//        httpMethod: HttpMethod,
//        callType: CallType,
//        path: String,
//        requestOptions: RequestOptions?,
//        body: String? = null,
//    ): T {
//        return execute(httpMethod, callType, path, requestOptions, body) {
//            val response = httpClient.request(it)
//            response.call.body()
//        }
//    }
//
//    /**
//     * Execute HTTP request (with retry strategy) and get a result as [T].
//     */
//    private suspend inline fun <T> execute(
//        httpMethod: HttpMethod,
//        callType: CallType,
//        path: String,
//        requestOptions: RequestOptions?,
//        body: String? = null,
//        block: (HttpRequestBuilder) -> T
//    ): T {
//        val hosts = callableHosts(callType)
//        val errors by lazy(LazyThreadSafetyMode.NONE) { mutableListOf<Throwable>() }
//        val requestBuilder = httpRequestBuilder(httpMethod, path, requestOptions, body)
//
//        for (host in hosts) {
//            requestBuilder.url.host = host.url
//            try {
//                setTimeout(requestBuilder, requestOptions, callType, host)
//                return block(requestBuilder).apply {
//                    mutex.withLock { host.reset() }
//                }
//            } catch (exception: Throwable) {
//                host.onError(exception)
//                errors += exception.asClientException()
//            }
//        }
//        throw AlgoliaRetryException(errors)
//    }
//
//    /**
//     * Get list of [RetryableHost] for a given [CallType].
//     */
//    suspend fun callableHosts(callType: CallType): List<RetryableHost> {
//        return mutex.withLock {
//            hosts.expireHostsOlderThan(hostStatusExpirationDelayMS)
//            val hostsCallType = hosts.filterCallType(callType)
//            val hostsCallTypeAreUp = hostsCallType.filter { it.isUp }
//            hostsCallTypeAreUp.ifEmpty {
//                hostsCallType.onEach { it.reset() }
//            }
//        }
//    }
//
//    /**
//     * Get a [HttpRequestBuilder] with given parameters.
//     */
//    private fun httpRequestBuilder(
//        httpMethod: HttpMethod,
//        path: String,
//        requestOptions: RequestOptions?,
//        body: String?,
//    ): HttpRequestBuilder {
//        return HttpRequestBuilder().apply {
//            url {
//                protocol = URLProtocol.HTTPS
//                port = URLProtocol.HTTPS.defaultPort
//                path(path)
//            }
//            method = httpMethod
//            credentialsOrNull?.let {
//                applicationId(it.applicationID)
//                apiKey(it.apiKey)
//            }
//            requestOptions(requestOptions)
//            body?.let { requestBody(it) }
//        }
//    }
//
//    private fun HttpRequestBuilder.requestBody(payload: String) {
//        val body: Any = when (compression) {
//            Compression.Gzip -> payload.let(Gzip::invoke)
//            Compression.None -> payload
//        }
//        setBody(body)
//    }
//
//    /**
//     * Handle API request exceptions.
//     */
//    private suspend fun RetryableHost.onError(throwable: Throwable) {
//        when (throwable) {
//            is CancellationException -> throw throwable // propagate coroutine cancellation
//            is ClientRequestException -> throw throwable.asApiException()
//            is HttpRequestTimeoutException, is SocketTimeoutException, is ConnectTimeoutException -> mutex.withLock { hasTimedOut() }
//            is IOException, is ResponseException -> mutex.withLock { hasFailed() }
//            else -> throw throwable.asClientException()
//        }
//    }
//
//    /**
//     * Set socket read/write timeout.
//     */
//    private fun setTimeout(
//        requestBuilder: HttpRequestBuilder,
//        requestOptions: RequestOptions?,
//        callType: CallType,
//        host: RetryableHost,
//    ) {
//        requestBuilder.timeout {
//            socketTimeoutMillis = requestOptions.getTimeout(callType) * (host.retryCount + 1)
//        }
//    }
//
//    override suspend fun <T> customRequest(
//        method: HttpMethod,
//        callType: CallType,
//        path: String,
//        responseType: TypeInfo,
//        body: String?,
//        requestOptions: RequestOptions?
//    ): T {
//        val httpResponse = request<HttpResponse>(method, callType, path, requestOptions, body)
//        return httpResponse.call.bodyAs(responseType)
//    }
//
//    /**
//     * Receive Http payload as [T].
//     */
//    @Suppress("UNCHECKED_CAST")
//    private suspend fun <T> HttpClientCall.bodyAs(type: TypeInfo): T = body(type) as T
//}

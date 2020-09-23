package com.algolia.instantsearch.insights.internal.webservice

import com.algolia.instantsearch.insights.internal.converter.ConverterEventInternalToString
import com.algolia.instantsearch.insights.internal.event.EventInternal
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

internal class WebServiceHttp(
    private val appId: String,
    private val apiKey: String,
    private val environment: Environment,
    private val connectTimeoutInMilliseconds: Int,
    private val readTimeoutInMilliseconds: Int,
) : WebService {

    override fun send(vararg event: EventInternal): WebService.Response {
        val array = JSONArray(ConverterEventInternalToString.convert(event.toList()).map(::JSONObject))
        val string = JSONObject().put("events", array).toString()

        val url = URL(environment.url)
        val connection = (url.openConnection() as HttpURLConnection).also {
            it.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
            it.setRequestProperty("Accept", "application/json")
            it.setRequestProperty("X-Algolia-Application-Id", appId)
            it.setRequestProperty("X-Algolia-API-Key", apiKey)
            it.setRequestProperty("Content-Length", string.length.toString())
            it.setRequestProperty(
                "User-Agent",
                computeUserAgent()
            )
            it.requestMethod = "POST"
            it.connectTimeout = connectTimeoutInMilliseconds
            it.readTimeout = readTimeoutInMilliseconds
            it.doOutput = true
            it.useCaches = false
        }
        connection.outputStream.write(string.toByteArray())
        val responseCode = connection.responseCode
        val errorMessage = connection.errorStream?.bufferedReader()?.readText()
        connection.disconnect()
        return WebService.Response(
            errorMessage = errorMessage,
            code = responseCode
        )
    }

    override fun toString(): String {
        return "WebServiceHttp(appId='$appId', apiKey='$apiKey', connectTimeoutInMilliseconds=$connectTimeoutInMilliseconds, readTimeoutInMilliseconds=$readTimeoutInMilliseconds)"
    }
}

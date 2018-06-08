package com.algolia.instantsearch.insights

import java.net.HttpURLConnection
import java.net.URL


internal class NetworkManager(
    private val appId: String,
    private val apiKey: String
) {

    fun sendEvent(event: Event): Int {
        val route = when (event) {
            is Event.Click -> EventType.Click.route
            is Event.View -> EventType.View.route
            is Event.Conversion -> EventType.Conversion.route
        }
        val string = ConverterParameterToString.convert(event.params)
        val url = URL("https://insights.algolia.io/1/searches/$route")
        val connection = (url.openConnection() as HttpURLConnection).also {
            it.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
            it.setRequestProperty("Accept", "application/json")
            it.setRequestProperty("X-Algolia-Application-Id", appId)
            it.setRequestProperty("X-Algolia-API-Key", apiKey)
            it.setRequestProperty("Content-Length", string.length.toString())
            it.requestMethod = "POST"
            it.connectTimeout = 5000
            it.readTimeout = 5000
            it.doOutput = true
            it.useCaches = false
        }
        connection.outputStream.write(string.toByteArray())
        val responseCode = connection.responseCode
        connection.disconnect()
        return responseCode
    }
}

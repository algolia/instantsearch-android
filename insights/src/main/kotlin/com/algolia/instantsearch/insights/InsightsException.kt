package com.algolia.instantsearch.insights


sealed class InsightsException : Exception() {

    class CredentialsNotFound : InsightsException()
}

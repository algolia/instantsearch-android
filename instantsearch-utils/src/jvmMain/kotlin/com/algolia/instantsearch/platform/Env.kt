package com.algolia.instantsearch.platform

/** Get current os version */
internal actual fun osVersion(): String = "JVM (${System.getProperty("java.version")})"

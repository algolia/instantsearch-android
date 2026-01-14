package com.algolia.instantsearch.migration2to3


public class EmptyStringException(name: String) : IllegalArgumentException("$name must not have an empty string value.")

@Deprecated("Replace with primitive value", ReplaceWith("String"))
public typealias Attribute = String


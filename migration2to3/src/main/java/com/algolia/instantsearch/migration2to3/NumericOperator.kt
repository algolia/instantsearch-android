package com.algolia.instantsearch.migration2to3

public enum class NumericOperator(public val raw: String) {
    Less("<"),
    LessOrEquals("<="),
    Equals("="),
    NotEquals("!="),
    GreaterOrEquals(">="),
    Greater(">")
}

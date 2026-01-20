package com.algolia.instantsearch.filter

import com.algolia.client.model.analytics.Operator

/**
 * Numeric comparison operators for filters.
 * Mapped from v3 Operator enum.
 */
public enum class NumericOperator(public val raw: String) {
    Less("<"),
    LessOrEquals("<="),
    Equals("="),
    NotEquals("!="),
    GreaterOrEquals(">="),
    Greater(">");

    public fun toV3Operator(): Operator = when (this) {
        Less -> Operator.LessThan
        LessOrEquals -> Operator.LessThanEqual
        Equals -> Operator.Equal
        NotEquals -> Operator.ExclamationEqual
        GreaterOrEquals -> Operator.GreaterThanEqual
        Greater -> Operator.GreaterThan
    }

    public companion object {
        public fun fromV3Operator(operator: Operator): NumericOperator? = when (operator) {
            Operator.LessThan -> Less
            Operator.LessThanEqual -> LessOrEquals
            Operator.Equal -> Equals
            Operator.ExclamationEqual -> NotEquals
            Operator.GreaterThanEqual -> GreaterOrEquals
            Operator.GreaterThan -> Greater
            Operator.Colon -> null // Not a numeric operator
        }
    }
}

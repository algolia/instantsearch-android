package com.algolia.instantsearch.filter

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

    public fun toV3Operator(): com.algolia.client.model.analytics.Operator = when (this) {
        Less -> com.algolia.client.model.analytics.Operator.LessThan
        LessOrEquals -> com.algolia.client.model.analytics.Operator.LessThanEqual
        Equals -> com.algolia.client.model.analytics.Operator.Equal
        NotEquals -> com.algolia.client.model.analytics.Operator.ExclamationEqual
        GreaterOrEquals -> com.algolia.client.model.analytics.Operator.GreaterThanEqual
        Greater -> com.algolia.client.model.analytics.Operator.GreaterThan
    }

    public companion object {
        public fun fromV3Operator(operator: com.algolia.client.model.analytics.Operator): NumericOperator? = when (operator) {
            com.algolia.client.model.analytics.Operator.LessThan -> Less
            com.algolia.client.model.analytics.Operator.LessThanEqual -> LessOrEquals
            com.algolia.client.model.analytics.Operator.Equal -> Equals
            com.algolia.client.model.analytics.Operator.ExclamationEqual -> NotEquals
            com.algolia.client.model.analytics.Operator.GreaterThanEqual -> GreaterOrEquals
            com.algolia.client.model.analytics.Operator.GreaterThan -> Greater
            com.algolia.client.model.analytics.Operator.Colon -> null // Not a numeric operator
        }
    }
}

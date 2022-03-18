package com.algolia.instantsearch.filter.state

/**
 * Indicates which type of boolean operator should be applied between each filters in the group.
 * For advanced filtering, you can read more about [filter grouping and boolean operators](https://www.algolia.com/doc/guides/managing-results/refine-results/filtering/in-depth/combining-boolean-operators/).
 */
public enum class FilterOperator {
    And,
    Or
}

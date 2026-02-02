package com.algolia.instantsearch.filter

internal fun interface Converter<I, O> {
    operator fun invoke(input: I): O
}

public sealed class FilterGroupsConverter<I, O> : Converter<I, O> {

    /**
     * Converts a [List] of [FilterGroup] to its SQL-like [String] representation.
     * Returns null if the list is empty.
     */
    public object SQL : FilterGroupsConverter<Set<FilterGroup<*>>, String?>() {

        override fun invoke(input: Set<FilterGroup<*>>): String? {
            return if (input.isNotEmpty()) {
                input.filterNot { it.isEmpty() }.joinToString(separator = " AND ") { group ->
                    val separator = when (group) {
                        is FilterGroup.And -> " AND "
                        is FilterGroup.Or -> " OR "
                    }
                    group.joinToString(prefix = "(", postfix = ")", separator = separator) { FilterConverter.SQL(it) }
                }
            } else null
        }

        /**
         * Same as [SQL], but removes quotes for readability purposes.
         */
        public object Unquoted : FilterGroupsConverter<Set<FilterGroup<*>>, String?>() {

            override fun invoke(input: Set<FilterGroup<*>>): String? {
                return SQL(input)?.replace("\"", "")
            }
        }
    }

    /**
     * Converts a [List] of [FilterGroup] to its legacy representation.
     */
    public sealed class Legacy<T : Filter> : FilterGroupsConverter<Set<FilterGroup<T>>, List<List<String>>>() {

        override fun invoke(input: Set<FilterGroup<T>>): List<List<String>> {
            return toLegacy(input, escape = true)
        }

        /**
         * Same as [Legacy], but without quotes.
         */
        @Suppress("FunctionName") // To keep DX consistency
        public fun Unquoted(groups: Set<FilterGroup<T>>): List<List<String>> {
            return toLegacy(groups, escape = false)
        }

        private fun toLegacy(groups: Set<FilterGroup<T>>, escape: Boolean): List<List<String>> {
            val (andEntries, orEntries) = groups.partition { it is FilterGroup.And }
            val ands = andEntries.flatMap { group -> group.flatMap { convertFilter(it, escape) }.map { listOf(it) } }
            val ors = orEntries.map { group -> group.flatMap { convertFilter(it, escape) } }

            return ands + ors
        }

        private fun <T : Filter> convertFilter(filter: T, escape: Boolean): List<String> {
            return if (escape) FilterConverter.Legacy(filter) else FilterConverter.Legacy.Unquoted(filter)
        }

        public object Facet : Legacy<Filter.Facet>()

        public object Tag : Legacy<Filter.Tag>()

        public object Numeric : Legacy<Filter.Numeric>()
    }
}

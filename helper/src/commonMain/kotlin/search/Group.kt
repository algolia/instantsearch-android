package search

import com.algolia.search.model.filter.Filter


sealed class Group<T : Filter> {

    abstract val name: String

    sealed class And<T : Filter> : Group<T>() {

        data class Any(override val name: String): And<Filter>()

        data class Tag(override val name: String): And<Filter.Tag>()

        data class Numeric(override val name: String): And<Filter.Numeric>()

        data class Facet(override val name: String): And<Filter.Facet>()
    }

    sealed class Or<T : Filter> : Group<T>() {

        data class Tag(override val name: String): Or<Filter.Tag>()

        data class Numeric(override val name: String): Or<Filter.Numeric>()

        data class Facet(override val name: String): Or<Filter.Facet>()
    }
}
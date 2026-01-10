package com.algolia.instantsearch.migration2to3

public sealed class FilterGroup<T : Filter> : Set<T> {

    protected abstract val filters: Set<T>

    public abstract val name: String?

    /**
     * Filters in this group will be evaluated together with the "AND" operator.
     */
    public sealed class And<T : Filter>(
        override val filters: Set<T>,
        override val name: String?
    ) : FilterGroup<T>(),
        Set<T> by filters {

        /**
         * Contains any type of [Filter].
         */
        public data class Any(
            override val filters: Set<Filter>,
            override val name: String? = null
        ) : And<Filter>(filters, name) {

            public constructor(vararg filters: Filter, name: String? = null) : this(filters.toSet(), name)
        }

        /**
         * Contains only [Filter.Facet].
         */
        public data class Facet(
            override val filters: Set<Filter.Facet>,
            override val name: String? = null
        ) : And<Filter.Facet>(filters, name) {

            public constructor(vararg filters: Filter.Facet, name: String? = null) : this(filters.toSet(), name)
        }

        /**
         * Contains only [Filter.Tag].
         */
        public data class Tag(
            override val filters: Set<Filter.Tag>,
            override val name: String? = null
        ) : And<Filter.Tag>(filters, name) {

            public constructor(vararg filters: Filter.Tag, name: String? = null) : this(filters.toSet(), name)
        }

        /**
         * Contains only [Filter.Numeric].
         */
        public data class Numeric(
            override val filters: Set<Filter.Numeric>,
            override val name: String? = null
        ) : And<Filter.Numeric>(filters, name) {

            public constructor(vararg filters: Filter.Numeric, name: String? = null) : this(filters.toSet(), name)
        }

        public data class Hierarchical(
            override val filters: Set<Filter.Facet>,
            val path: List<Filter.Facet>,
            val attributes: List<Attribute>,
            override val name: String? = null
        ) : And<Filter.Facet>(filters, name)
    }

    /**
     * Filters in this group will be evaluated together with the "OR" operator.
     */
    public sealed class Or<T : Filter>(
        override val filters: Set<T>,
        override val name: String?
    ) : FilterGroup<T>(), Set<T> by filters {

        /**
         * Contains only [Filter.Facet].
         */
        public data class Facet(
            override val filters: Set<Filter.Facet>,
            override val name: String? = null
        ) : Or<Filter.Facet>(filters, name) {

            public constructor(vararg filters: Filter.Facet, name: String? = null) : this(filters.toSet(), name)
        }

        /**
         * Contains only [Filter.Tag].
         */
        public data class Tag(
            override val filters: Set<Filter.Tag>,
            override val name: String? = null
        ) : Or<Filter.Tag>(filters, name) {

            public constructor(vararg filters: Filter.Tag, name: String? = null) : this(filters.toSet(), name)
        }

        /**
         * Contains only [Filter.Numeric].
         */
        public data class Numeric(
            override val filters: Set<Filter.Numeric>,
            override val name: String? = null
        ) : Or<Filter.Numeric>(filters, name) {

            public constructor(vararg filters: Filter.Numeric, name: String? = null) : this(filters.toSet(), name)
        }
    }
}

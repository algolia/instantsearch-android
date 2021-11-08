package com.algolia.instantsearch

/**
 * This annotation marks a library API as experimental.
 *
 * Any usage of a declaration annotated with `@ExperimentalInstantSearch` must be accepted either by
 * annotating that usage with the [OptIn] annotation, e.g. `@OptIn(ExperimentalInstantSearch::class)`,
 * or by using the compiler argument `-Xopt-in=com.algolia.instantsearch.core.ExperimentalInstantSearch`.
 */
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.LOCAL_VARIABLE,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.TYPEALIAS
)
@Retention(AnnotationRetention.BINARY)
@RequiresOptIn(message = "This Instant Search API is experimental, It can be incompatibly changed in the future.")
public annotation class ExperimentalInstantSearch

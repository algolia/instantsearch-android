package com.algolia.instantsearch

/**
 * This annotation marks a library API as experimental.
 *
 * Any usage of a declaration annotated with `@ExperimentalInstantSearch` must be accepted either by
 * annotating that usage with the [OptIn] annotation, e.g. `@OptIn(ExperimentalInstantSearch::class)`,
 * or by using the compiler argument `-Xopt-in=com.algolia.instantsearch.ExperimentalInstantSearch`.
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
@RequiresOptIn(message = "This Instant Search API is experimental, It can be incompatibly changed in the future.")
public annotation class ExperimentalInstantSearch

/**
 * API marked with this annotation is internal, and it is not intended to be used outside Instant Search.
 * It could be modified or removed without any notice. Using it could cause undefined behaviour and/or any unexpected
 * effects.
 */
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.TYPEALIAS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message = "This API is internal in InstantSearch and should not be used. It could be removed or changed without notice."
)
public annotation class InternalInstantSearch

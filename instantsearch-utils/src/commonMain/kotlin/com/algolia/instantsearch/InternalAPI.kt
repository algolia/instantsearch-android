package com.algolia.instantsearch

/**
 * API marked with this annotation is internal, and it is not intended to be used outside Instant Search.
 * It could be modified or removed without any notice. Using it could cause undefined behaviour and/or any unexpected
 * effects.
 */
@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message = "This API is internal in Instant Search and should not be used. It could be removed or changed without notice."
)
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
public annotation class InternalAPI

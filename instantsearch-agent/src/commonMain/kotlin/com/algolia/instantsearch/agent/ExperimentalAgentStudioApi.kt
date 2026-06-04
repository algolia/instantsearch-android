package com.algolia.instantsearch.agent

/**
 * Marks an API of the InstantSearch Agent Studio library as **experimental**.
 *
 * The Agent Studio library is a standalone, early-stage module versioned
 * independently (0.x) from the main InstantSearch library. Anything annotated
 * with [ExperimentalAgentStudioApi] can change in source- and binary-
 * incompatible ways — including being renamed or removed — without a major
 * version bump, until the library reaches a stable 1.0 release.
 *
 * To use an experimental declaration you must explicitly opt in, either by
 * annotating the usage site:
 *
 * ```
 * @OptIn(ExperimentalAgentStudioApi::class)
 * fun startChat() { /* ... */ }
 * ```
 *
 * or, for a whole module, by adding the compiler argument
 * `-opt-in=com.algolia.instantsearch.agent.ExperimentalAgentStudioApi`.
 *
 * We deliberately keep this marker local to the Agent Studio module (rather
 * than reusing `com.algolia.instantsearch.ExperimentalInstantSearch`) so the
 * standalone library doesn't couple its opt-in surface to the main
 * InstantSearch version.
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
    AnnotationTarget.TYPEALIAS,
)
@Retention(AnnotationRetention.BINARY)
@RequiresOptIn(
    level = RequiresOptIn.Level.WARNING,
    message = "InstantSearch Agent Studio is experimental. This API can be changed " +
        "incompatibly or removed in any release before 1.0. Opt in with " +
        "@OptIn(ExperimentalAgentStudioApi::class) to acknowledge this.",
)
public annotation class ExperimentalAgentStudioApi

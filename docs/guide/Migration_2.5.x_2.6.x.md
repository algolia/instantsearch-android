# Migrate from 2.5.x to 2.6.x

Version `1.5.x` of the library uses the latest Kotlin version `1.4.0` and Kotlin serialization `1.0.0`. If you want to migrate from 
`2.5.x` to `2.6.x` please follow these steps:

### Gradle
The library now uses the full power of [Gradle metadata module](https://blog.gradle.org/gradle-metadata-1.0).

This means you can keep using the android module like before:
```groovy
implementation "com.algolia:instantsearch-android:$instantsearch_version"
```
Optionally, if you are using Gradle version 6.0 or above, you can use the root module:
```groovy
implementation "com.algolia:instantsearch:$instantsearch_version"
```

### Serialization
A significant part of the public API was renamed or extracted to a separate package.
Here is the most important parts from the kotlinx serialization's [migration guide](https://github.com/Kotlin/kotlinx.serialization/blob/1.0.0-RC/docs/migration.md):

> During the preparation of serialization 1.0.0 release, most of the API has been changed, renamed, moved to a separate 
> package or made internal. IDEA migrations were introduced, but unfortunately not all API can be migrated with automatic 
> replacements.
> 
> To simplify your migrations path, it is recommended to enable star imports in IDE (so all extensions are imported automatically) first.
> 
> * Start applying replacements for the deprecated code
> * If some signatures are not resolved, try to hit `alt + Enter` and import the signature
> * If methods are still not resolved, it is recommended to use star imports for kotlinx.serialization signatures in the problematic file


<img src="docs/banner.png" alt="InstantSearch Android" />

[ ![Download](https://img.shields.io/maven-central/v/com.algolia/instantsearch-android?label=Download) ](https://search.maven.org/search?q=a:instantsearch-android)
[![Build Status](https://travis-ci.org/algolia/instantsearch-android.svg?branch=master)](https://travis-ci.org/algolia/instantsearch-android)

InstantSearch family: **InstantSearch Android** | [InstantSearch iOS][instantsearch-ios-github] | [React InstantSearch][react-instantsearch-github] | [InstantSearch.js][instantsearch-js-github] | [Angular InstantSearch][instantsearch-angular-github] | [Vue InstantSearch][instantsearch-vue-github].

**InstantSearch Android** is a library providing widgets and helpers to help you build the best instant-search experience on Android with Algolia.
It is built on top of Algolia's [Kotlin API Client][kotlin-client] to provide you a high-level solution to quickly build various search interfaces.

# Showcases

You can see InstantSearch Android in action in our [examples repository][instantsearch-android-examples], have a look at it to see concrete examples of all the available widgets.

# Usage

You can add InstantSearch to your Android application by adding the following line to your `build.gradle`'s dependencies.
```groovy
implementation "com.algolia:instantsearch-android:$instantsearch_version"
// or, for Compose UI
implementation "com.algolia:instantsearch-compose:$instantsearch_version"
```
<!--TODO Document using com.algolia.instantsearch.helper.helper-jvm / using core directly -->

⚠️ Important: starting from version `2.4.0`, the library is compatible only with kotlin version `1.3.70` or higher; for previous versions of kotlin, please use version `2.3.1` of the library.  
ℹ️ Please follow the [migration guide](docs/guide/Migration_2.5.x_2.6.x.md) to migrate from `2.5.x` or below to the latest version.

See the [documentation][doc]. You can start with the [Getting Started Guide][getting-started].

### Insights

You can add **InstantSearch Insights** to your Android application by adding the following line to your `build.gradle`'s dependencies.
```groovy
implementation "com.algolia:instantsearch-insights-android:$instantsearch_version"
```

Please refer to the [library](instantsearch-insights/README.md) for more details.

### R8 / Proguard rules

If you use this library in an Android project which uses R8, there is nothing you have to do. The specific rules are 
already bundled into the JAR, which can be interpreted by R8 automatically.

If however, you don’t use R8, then you might need rules from [Algolia Kotlin Client](https://github.com/algolia/algoliasearch-client-kotlin#r8--proguard-rules) which is a dependency of this library.

## Telemetry
In order to inform how InstantSearch can be improved and prioritize its future development, the following data points are collected:

- version of InstantSearch
- the name of the instantiated InstantSearch components (e.g. `HitsSearcher`, `FilterState`)
- the name of the components for which the default value has been overridden - this excludes the value itself, that do not get tracked. (e.g. By default the `isDisjunctiveFacetingEnabled` value is set to true, but in the case it gets instantiated to false, then the telemetry will track that the `isDisjunctiveFacetingEnabled` parameter received a custom value)

We **do not** collect any metrics which may contain sensitive data, but you can opt out at any time by passing: `Telemetry.shared.enabled = false`

## Contributing

From [reporting bugs or missing functionality](https://github.com/algolia/instantsearch-android/issues/new) to [fixing a typo or proposing an improvement](https://github.com/algolia/instantsearch-android/compare), all contributions are welcome! Read the [Contributing Guide](https://github.com/algolia/instantsearch-android/blob/master/CONTRIBUTING.md) to setup your development environment.

## Troubleshooting

Encountering an issue? Before reaching out to support, we recommend heading to our [FAQ](https://www.algolia.com/doc/guides/building-search-ui/troubleshooting/faq/android/) where you will find answers for the most common issues and gotchas with the library.

# License

InstantSearch Android is [licensed under Apache V2](LICENSE).

[doc]: https://algolia.com/doc/guides/building-search-ui/what-is-instantsearch/android/
[getting-started]: https://algolia.com/doc/guides/building-search-ui/getting-started/android/
[kotlin-client]: https://github.com/algolia/algoliasearch-client-kotlin
[media-gif]: ./docs/media.gif
[ecommerce-gif]: ./docs/ecommerce.gif
[media-url]: https://github.com/algolia/instantsearch-android-examples/tree/master/media
[ecommerce-url]: https://github.com/algolia/instantsearch-android-examples/tree/master/ecommerce
[showcase-url]: https://algolia.com/doc/guides/building-search-ui/widgets/showcase/android/
[examples-url]: https://github.com/algolia/instantsearch-android-examples
[react-instantsearch-github]: https://github.com/algolia/react-instantsearch/
[instantsearch-ios-github]: https://github.com/algolia/instantsearch-ios
[instantsearch-js-github]: https://github.com/algolia/instantsearch.js
[instantsearch-vue-github]: https://github.com/algolia/vue-instantsearch
[instantsearch-angular-github]: https://github.com/algolia/angular-instantsearch

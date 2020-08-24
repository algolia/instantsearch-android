
<img src="docs/banner.png" alt="InstantSearch Android" />

[ ![Download](https://api.bintray.com/packages/algolia/maven/com.algolia%3Ainstantsearch-android/images/download.svg) ](https://bintray.com/algolia/maven/com.algolia%3Ainstantsearch-android/_latestVersion)
[![Build Status](https://travis-ci.org/algolia/instantsearch-android.svg?branch=master)](https://travis-ci.org/algolia/instantsearch-android)

InstantSearch family: **InstantSearch Android** | [InstantSearch iOS][instantsearch-ios-github] | [React InstantSearch][react-instantsearch-github] | [InstantSearch.js][instantsearch-js-github] | [Angular InstantSearch][instantsearch-angular-github] | [Vue InstantSearch][instantsearch-vue-github].

**InstantSearch Android** is a library providing widgets and helpers to help you build the best instant-search experience on Android with Algolia.
It is built on top of Algolia's [Kotlin API Client][kotlin-client] to provide you a high-level solution to quickly build various search interfaces.


<!--
# Demo
You can see InstantSearch Android in action in our [Examples repository][examples-url], in which we published two example apps built with InstantSearch:

| [Media app][media-url] | [E-commerce app][ecommerce-url] |
| --- | --- |
| [![animated screenshot of media app][media-gif]][media-url] | [![animated screenshot of e-commerce app][ecommerce-gif]][ecommerce-url] |

Have a look at our [widget showcase][showcase-url] to see concrete examples of all the available widgets.
-->

# Usage

You can add InstantSearch to your Android application by adding the following line to your `build.gradle`'s dependencies.
```groovy
implementation "com.algolia:instantsearch-android:$instantsearch_version"
```
<!--TODO Document using helper-jvm / using core directly -->

⚠️ Important: starting from version `2.4.0`, the library is compatible only with kotlin version `1.3.70` or higher; for previous versions of kotlin, please use version `2.3.1` of the library.  
ℹ️ Please follow the [migration guide](docs/guide/Migration_2.4.x_2.5.x.md) to migrate from `2.5.x` or below to the latest version.

See the [documentation][doc]. You can start with the [Getting Started Guide][getting-started].

# Contributing

From [reporting bugs or missing functionality](https://github.com/algolia/instantsearch-android/issues/new) to [fixing a typo or proposing an improvement](https://github.com/algolia/instantsearch-android/compare), all contributions are welcome! Read the [Contributing Guide](https://github.com/algolia/instantsearch-android/blob/master/CONTRIBUTING.md) to setup your development environment.

### Proguard rules

When proguard `minifyEnabled` option is set to `true` , you might get this error:

```
Can't locate argument-less serializer for class e.a.b.g.n.c (Kotlin reflection is not available). For generic classes, such as lists, please provide serializer explicitly.
```

Add this proguard rule to solve it.

```
-keep class com.algolia.search.model.** { *; }
```

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

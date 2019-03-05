<img src="docgen/assets/img/instant-search-android.png" alt="InstantSearch Android" />

[![Build Status](https://travis-ci.org/algolia/instantsearch-android.svg?branch=master)](https://travis-ci.org/algolia/instantsearch-android)

InstantSearch family: **InstantSearch Android** | [InstantSearch iOS][instantsearch-ios-github] | [React InstantSearch][react-instantsearch-github] | [InstantSearch.js][instantsearch-js-github] | [Angular InstantSearch][instantsearch-angular-github] | [Vue InstantSearch][instantsearch-vue-github].


**InstantSearch Android** is a library providing widgets and helpers to help you build the best instant-search experience on Android with Algolia.
It is built on top of Algolia's [Android API Client](https://github.com/algolia/algoliasearch-client-android) to provide you a high-level solution to quickly build various search interfaces.

# Demo
You can see InstantSearch Android in action in our [Examples repository](https://github.com/algolia/instantsearch-android-examples), in which we published two example apps built with InstantSearch:

| [Media app][media-url] | [E-commerce app][ecommerce-url] |
| --- | --- |
| [![animated screenshot of media app][media-gif]][media-url] | [![animated screenshot of e-commerce app][ecommerce-gif]][ecommerce-url] |


[media-gif]: ./docgen/assets/img/media.gif
[ecommerce-gif]: ./docgen/assets/img/ecommerce.gif
[media-url]: https://github.com/algolia/instantsearch-android-examples/tree/master/media
[ecommerce-url]: https://github.com/algolia/instantsearch-android-examples/tree/master/ecommerce

# Usage

You can add InstantSearch to your Android application by adding one of the following to your `build.gradle`'s dependencies. **Note that until a [Bug in Jetifier](https://issuetracker.google.com/issues/119776863) is resolved, if you use [AndroidX](https://developer.android.com/jetpack/androidx/) you should target our `instantsearch-androidx` artifacts**.
```groovy
implementation 'com.algolia:instantsearch-android:1.15.2'


// OR, if your application uses AndroidX
implementation 'com.algolia:instantsearch-androidx:1.15.2'


```

To use InstantSearch's Core without the UI components and helpers, you should use this instead:
```groovy
implementation 'com.algolia:instantsearch-android-core:1.15.2'


// OR, if your application uses AndroidX
implementation 'com.algolia:instantsearch-androidx-core:1.15.2'


```

See the [dedicated documentation website](https://community.algolia.com/instantsearch-android).

You can start with the [Getting Started Guide](https://community.algolia.com/instantsearch-android/getting-started).

# Contributing

From [reporting bugs or missing functionality](https://github.com/algolia/instantsearch-android/issues/new) to [fixing a typo or proposing an improvement](https://github.com/algolia/instantsearch-android/compare), all contributions are welcome! Read the [Contributing Guide](https://github.com/algolia/instantsearch-android/blob/master/CONTRIBUTING.md) to setup your development environment.

# License

InstantSearch Android is [MIT licensed](LICENSE.md).

[react-instantsearch-github]: https://github.com/algolia/react-instantsearch/
[instantsearch-ios-github]: https://github.com/algolia/instantsearch-ios
[instantsearch-js-github]: https://github.com/algolia/instantsearch.js
[instantsearch-vue-github]: https://github.com/algolia/vue-instantsearch
[instantsearch-angular-github]: https://github.com/algolia/angular-instantsearch


---
title: Examples
layout: examples.pug
name: examples
category: main
withHeadings: true
navWeight: 0
---

We made two demo applications to give you an idea of what you can build with InstantSearch Android:

## [Media application][media-url]
<img align="right" src="assets/img/media.gif" />

This example mimics the classical video search interface, with a modal to refine your search.

- Search in **video's title**
- Filter by *number of views*, *rating*, *video quality* or *captioning* 
- Automatic focus on the SearchBox
- Automatic closing of the keyboard when scrolling through the videos
- Filtering in a `DialogFragment` applied only when the users submit their final filters

<br />
<br />
<br />
<br />
<br />
<br />
<br />

## [E-commerce application][ecommerce-url]
<img align="right" src="assets/img/ecommerce.gif" />

This example imitates a product search interface like well-known e-commerce applications.

- Search in the **product's name**, **seller's name**, and **category** 
- Filter by *number of views*, *rating*, *video quality* or *captioning* 
- Custom views using [`AlgoliaHitView`](https://github.com/algolia/instantsearch-android/blob/master/instantsearch/src/main/java/com/algolia/instantsearch/ui/views/AlgoliaHitView.java) for displaying the promotions, ratings, ...
- Filtering in a `PopupWindow` with immediate feedback to let the user see its influence on the search results

[media-url]: https://github.com/algolia/instantsearch-android-examples/tree/master/media
[ecommerce-url]: https://github.com/algolia/instantsearch-android-examples/tree/master/ecommerce

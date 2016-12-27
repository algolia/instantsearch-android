# metalsmith-layouts

[![npm version][version-badge]][version-url]
[![build status][build-badge]][build-url]
[![dependency status][dependency-badge]][dependency-url]
[![devdependency status][devdependency-badge]][devdependency-url]
[![downloads][downloads-badge]][downloads-url]

> A metalsmith plugin for layouts

[![stack overflow][stackoverflow-badge]][stackoverflow-url]
[![slack chat][slack-badge]][slack-url]

This plugin allows you to apply layouts to your source files. It passes your source files to the selected layout as the variable `contents` and renders the result with the templating engine of your choice. You can use any templating engine supported by [consolidate.js](https://github.com/tj/consolidate.js#supported-template-engines).

For support questions please use [stack overflow][stackoverflow-url] or our [slack channel][slack-url]. For templating engine specific questions try the aforementioned channels, as well as the documentation for [consolidate.js](https://github.com/tj/consolidate.js) and your templating engine of choice.

## Installation

```bash
$ npm install metalsmith-layouts
```

## Example

Configuration in `metalsmith.json`:

```json
{
  "plugins": {
    "metalsmith-layouts": {
      "engine": "handlebars"
    }
  }
}
```

Source file `src/index.html`:

```html
---
layout: layout.html
title: The title
---
<p>The contents</p>
```

Layout `layouts/layout.html`:

```html
<!doctype html>
<html>
<head>
  <title>{{title}}</title>
</head>
<body>
  {{{contents}}}
</body>
</html>
```

Results in `build/index.html`:

```html
<!doctype html>
<html>
<head>
  <title>The title</title>
</head>
<body>
  <p>The contents</p>
</body>
</html>
```

This is a very basic example. For more elaborate examples see the [metalsmith tag on stack overflow][stackoverflow-url].

## Options

You can pass options to `metalsmith-layouts` with the [Javascript API](https://github.com/segmentio/metalsmith#api) or [CLI](https://github.com/segmentio/metalsmith#cli). The options are:

* [engine](#engine): templating engine (required)
* [default](#default): default template (optional)
* [directory](#directory): directory for the layouts, layouts by default (optional)
* [partials](#partials): directory for the partials (optional)
* [pattern](#pattern): only files that match this pattern will be processed (optional)
* [rename](#rename): change the file extension of processed files to `.html` (optional)

### engine

The engine that will render your layouts. Metalsmith-layouts uses [consolidate.js](https://github.com/tj/consolidate.js) to render templating syntax, so any engine [supported by consolidate.js](https://github.com/tj/consolidate.js#supported-template-engines) can be used. Don't forget to install the templating engine separately. So this `metalsmith.json`:

```json
{
  "plugins": {
    "metalsmith-layouts": {
      "engine": "swig"
    }
  }
}
```

Will render your layouts with swig.

### default

The default layout to use. Can be overridden with the `layout` key in each file's YAML frontmatter, by passing either a layout or `false`. Passing `false` will skip the file entirely.

If a `default` layout has been specified, `metalsmith-layouts` will process all files unless a pattern has been passed. Don't forget to specify the default template's file extension. So this `metalsmith.json`:

```json
{
  "plugins": {
    "metalsmith-layouts": {
      "engine": "swig",
      "default": "default.html"
    }
  }
}
```

Will apply the `default.html` layout to all files, unless overridden in the frontmatter.

### directory

The directory where `metalsmith-layouts` looks for the layouts. By default this is `layouts`. So this `metalsmith.json`:

```json
{
  "plugins": {
    "metalsmith-layouts": {
      "engine": "swig",
      "directory": "templates"
    }
  }
}
```

Will look for layouts in the `templates` directory, instead of in `layouts`.

### partials

The directory where `metalsmith-layouts` looks for partials. Each partial is named by removing the file extension from its path (relative to the partials directory), so make sure to avoid duplicates. So this `metalsmith.json`:

```json
{
  "plugins": {
    "metalsmith-layouts": {
      "engine": "handlebars",
      "partials": "partials"
    }
  }
}
```

Would mean that a partial at `partials/nav.html` can be used in layouts as `{{> nav }}`, and `partials/nested/footer.html` can be used as `{{> nested/footer }}`. Note that passing anything but a string to the `partials` option will pass the option on to consolidate.

Make sure to check [consolidate.js](https://github.com/tj/consolidate.js) and your templating engine's documentation for guidelines on how to use partials.

### pattern

Only files that match this pattern will be processed. So this `metalsmith.json`:

```json
{
  "plugins": {
    "metalsmith-layouts": {
      "engine": "handlebars",
      "pattern": "**/*.hbs"
    }
  }
}
```

Would process all files that have the `.hbs` extension. Beware that the extensions might be changed by other plugins in the build chain, preventing the pattern from matching.
We use [multimatch](https://github.com/sindresorhus/multimatch) for the pattern matching.

### rename

Change the file extension of processed files to `.html` (optional). This option is set to `false` by default. So for example this `metalsmith.json`:

```json
{
  "plugins": {
    "metalsmith-layouts": {
      "engine": "handlebars",
      "rename": true
    }
  }
}
```

Would rename the extensions of all processed files to `.html`.

### exposeConsolidate

Not available over the `metalsmith.json` file.
Exposes Consolidate.requires as a function.

```js
// ...
.use(layout('swig', {
  exposeConsolidate: function(requires) {
    // your code here
  }
}))
// ...
```


### Consolidate

Any unrecognised options will be passed on to consolidate.js. You can use this, for example, to disable caching by passing `cache: false`. See the [consolidate.js documentation](https://github.com/tj/consolidate.js) for all options supported by consolidate.

## Origins

This plugin is a fork of the now deprecated [metalsmith-templates](https://github.com/segmentio/metalsmith-templates). Splitting up `metalsmith-templates` into two plugins was suggested by Ian Storm Taylor. The results are:

* [metalsmith-in-place](https://github.com/superwolff/metalsmith-in-place): render templating syntax in your source files.
* [metalsmith-layouts](https://github.com/superwolff/metalsmith-layouts): apply layouts to your source files.

## License

MIT

[build-badge]: https://travis-ci.org/superwolff/metalsmith-layouts.svg
[build-url]: https://travis-ci.org/superwolff/metalsmith-layouts
[dependency-badge]: https://david-dm.org/superwolff/metalsmith-layouts.svg
[dependency-url]: https://david-dm.org/superwolff/metalsmith-layouts
[devdependency-badge]: https://david-dm.org/superwolff/metalsmith-layouts/dev-status.svg
[devdependency-url]: https://david-dm.org/superwolff/metalsmith-layouts#info=devDependencies
[downloads-badge]: https://img.shields.io/npm/dm/metalsmith-layouts.svg
[downloads-url]: https://www.npmjs.com/package/metalsmith-layouts
[slack-badge]: https://img.shields.io/badge/Slack-Join%20Chat%20→-blue.svg
[slack-url]: http://metalsmith-slack.herokuapp.com/
[stackoverflow-badge]: https://img.shields.io/badge/stack%20overflow-%23metalsmith-red.svg
[stackoverflow-url]: http://stackoverflow.com/questions/tagged/metalsmith
[version-badge]: https://img.shields.io/npm/v/metalsmith-layouts.svg
[version-url]: https://www.npmjs.com/package/metalsmith-layouts

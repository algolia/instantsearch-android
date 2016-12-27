metalsmith-sass
===============

[![Build Status](https://travis-ci.org/stevenschobert/metalsmith-sass.svg?branch=master)](https://travis-ci.org/stevenschobert/metalsmith-sass)
[![Dependency Status](https://gemnasium.com/stevenschobert/metalsmith-sass.svg)](https://gemnasium.com/stevenschobert/metalsmith-sass)

A Sass plugin for Metalsmith.

## Installation

```sh
npm install --save metalsmith-sass
```

## Getting Started

If you haven't checked out [Metalsmith](http://metalsmith.io/) before, head over to their website and check out the
documentation.

## CLI Usage

If you are using the command-line version of Metalsmith, you can install via npm, and then add the
`metalsmith-sass` key to your `metalsmith.json` file:

```json
{
  "plugins": {
    "metalsmith-sass": {
      "outputStyle": "expanded"
    }
  }
}
```

## JavaScript API

If you are using the JS Api for Metalsmith, then you can require the module and add it to your
`.use()` directives:

```js
var sass = require('metalsmith-sass');

metalsmith.use(sass({
  outputStyle: "expanded"
}));
```

## Options

See [node-sass](https://github.com/andrew/node-sass) for a complete list of supported options.

In addition to the options that node-sass provides, metalsmith-sass provides the following options:

### outputDir

Change the base folder path styles are outputed to. You can use this in combination with
Metalsmith's `destination` option to control where styles end up after the build.

The final output directory is equal to `Metalsmith.destination() + outputDirOption`. For example,
the following setup output styles to `build/css/` even though the source files are in `src/scss/`:

```js
Metalsmith()
  .source("src/")
  .destination("build/")
  .use(sass({
    outputDir: 'css/'   // This changes the output dir to "build/css/" instead of "build/scss/"
  }))
  .build(function () {
    done();
  });
```

As of version [v1.1](https://github.com/stevenschobert/metalsmith-sass/releases/v1.1.0), you can also use a function to dynamically manipulate the output dir.

This is useful if you want to preserve your folder structure, but change just one folder name.

```js
Metalsmith()
  .source("src/")
  .destination("build/")
  .use(sass({
    outputDir: function(originalPath) { 
      // this will change scss/some/path to css/some/path
      return originalPath.replace("scss", "css");
    }
  }))
  .build(function () {
    done();
  });
```

## Source Maps

The easiest way to enable source maps in your metalsmith project is to add the following options:

```js
Metalsmith()
  .source("src/")
  .destination("build/")
  .use(sass({
    sourceMap: true,
    sourceMapContents: true   // This will embed all the Sass contents in your source maps.
  }))
  .build(function () {
    done();
  });
```

Though the `sourceMapContents` is not required, I recommend adding it, otherwise you'll need to
manually serve up your `.scss` files along with your compiled `.css` files when you publish your
site.

## .sass files

As of version [v1.2](https://github.com/stevenschobert/metalsmith-sass/releases/v1.2.0),
metalsmith-sass automatically handles `.sass` files, so you don't need to specify the `indentedSyntax`
option. Though you might still need set options for `indentType` and `indentWidth` if you are
using something other than 2 spaces for indentation.

## Credits

Thanks to [Segment.io](http://github.com/segmentio) for creating and open-sourcing
[Metalsmith](https://github.com/segmentio/metalsmith)! Also thanks to the whole community behind
the [node-sass](https://github.com/andrew/node-sass) project.

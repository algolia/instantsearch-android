# ms-webpack

[![Build Status](https://travis-ci.org/nealgranger/ms-webpack.svg?branch=master)](https://travis-ci.org/nealgranger/ms-webpack)
[![Dependency Status](https://gemnasium.com/nealgranger/ms-webpack.svg)](https://gemnasium.com/nealgranger/ms-webpack)
[![Coverage Status](https://coveralls.io/repos/nealgranger/ms-webpack/badge.svg?branch=master&service=github)](https://coveralls.io/github/nealgranger/ms-webpack?branch=master)

A [webpack][webpack] plugin for [Metalsmith][metalsmith].
## Installation

add to your package.js dependencies
```
npm install --save ms-webpack
```

## Usage

```js
var webpack = require('ms-webpack')

var config = {
  context: './src/assets/',
  entry: {
    main: ['./js/main.js', './css/main.css'],
    vendor: './js/ventor.js'
  },
  output: {
    path: './build',
    publicPath: '/',
    filename: 'js/[name].[chunkhash].js'
  },
  // ...
}

Metalsmith(__dirname)
  .ignore('assets')
  .use(webpack(config))
  .build();
```
It is necessary to manually use `ignore()` to prevent metalsmith from copying the files referenced by webpack if they are within the metalsmith source directory.

### Options

See the [webpack configuration][webpack configuration] documentation for details.

### Referencing compiled files in templates

ms-webpack populates metalsmith metadata with the output file paths from webpack. If your output file names are dynamic, this provides a way to automatically resolve them in your template.

`metadata.webpack.assets` maps of all source file names to their corresponding output files. eg:
```js
{
  "main.js": "/js/main.1234567890.js",
  "main.css": "/css/main.1234567890.css",
  "vendor.js": "/js/vendor.654210987.js"
}
```
`metadata.webpack.assetsByType` is a map of all output files sorted by file extension. eg:
```js
{
  "js": ["/js/main.1234567890.js", "/js/vendor.654210987.js"]
  "css": ["/css/main.7654321098.css"]
}
```
example medatada use in a template
```jade
html
  head
    - var styleSheets = webpack.assetsByType.css
    if styleSheets
      each file in styleSheets
        link(rel="stylesheet" href=file)
  body
    //- ...
    - var scripts = webpack.assetsByType.js
    if scripts
      each file in scripts
        script(src=file)

```
## Development

Compile and watch with `$ npm run dev`

## License

MIT License, see [LICENSE][license] for details.

[metalsmith]: http://www.metalsmith.io/
[license]: LICENSE.md
[webpack]: http://webpack.github.io/
[webpack configuration]: http://webpack.github.io/docs/configuration.html


# metalsmith-headings

  A Metalsmith plugin that extracts headings from HTML files and attaches them to the file's metadata.

## Installation

    $ npm install metalsmith-headings

## Example

```js
var Metalsmith = require('metalsmith');
var headings = require('metalsmith-headings');

Metalsmith(__dirname)
  .use(headings('h2'))
  .build();
```

## License

  MIT
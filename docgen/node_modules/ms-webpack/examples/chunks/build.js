var metalsmith = require('metalsmith');

// Metalsmith plugins
var webpack = require('../..');
var inPlace = require('metalsmith-in-place');
var rename = require('metalsmith-rename');

metalsmith(__dirname)
  .ignore('js')
  .use(webpack(require('./webpack.config.js')))
  .use(inPlace({
    pattern: '*.jade',
    engine: 'jade'
  }))
  .use(rename([[/\.jade$/, '.html']]))
  .build(function(err) {
    if (err) {
      throw err;
    }
  });

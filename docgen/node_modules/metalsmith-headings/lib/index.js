
var cheerio = require('cheerio');
var extname = require('path').extname;

/**
 * Expose `plugin`.
 */

module.exports = plugin;

/**
 * Get the headings from any html files.
 *
 * @param {String or Object} options (optional)
 *   @property {Array} selectors
 */

function plugin(options){
  if ('string' == typeof options) options = { selectors: [options] };
  options = options || {};
  var selectors = options.selectors || ['h2'];

  return function(files, metalsmith, done){
    setImmediate(done);
    Object.keys(files).forEach(function(file){
      if ('.html' != extname(file)) return;
      var data = files[file];
      var contents = data.contents.toString();
      var $ = cheerio.load(contents);
      data.headings = [];

      selectors.forEach(function(s){
        $(s).each(function(){
          data.headings.push({
            id: $(this).attr('id'),
            tag: $(this)[0].name,
            text: $(this).text()
          });
        });
      });
    });
  };
}
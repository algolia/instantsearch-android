/**
 * Dependencies
 */
var consolidate = require('consolidate');
var debug = require('debug')('metalsmith-layouts');
var each = require('async').each;
var extend = require('extend');
var omit = require('lodash.omit');
var path = require('path');

/**
 * Helpers
 */
var check = require('./helpers/check');
var readPartials = require('./helpers/read-partials');

/**
 * Expose `plugin`.
 */
module.exports = plugin;

/**
 * Settings
 *
 * Options supported by metalsmith-layouts
 */
var settings = [
  'default',
  'directory',
  'engine',
  'partials',
  'pattern',
  'rename',
  'exposeConsolidate'
];

/**
 * Metalsmith plugin to run files through any layout in a layout `dir`.
 *
 * @param {String or Object} options
 *   @property {String} default (optional)
 *   @property {String} directory (optional)
 *   @property {String} engine
 *   @property {String} partials (optional)
 *   @property {String} pattern (optional)
 *   @property {Boolean} rename (optional)
 * @return {Function}
 */
function plugin(opts){
  /**
   * Init
   */
  opts = opts || {};

  // Accept string option to specify the engine
  if (typeof opts === 'string') {
    opts = {engine: opts};
  }

  // An engine should be specified
  if (!opts.engine) {
    throw new Error('"engine" option required');
  }

  // Throw an error for unsupported engines or typos
  if (!consolidate[opts.engine]) {
    throw new Error('Unknown template engine: "' + opts.engine + '"');
  }

  if (typeof opts.exposeConsolidate === 'function') {
    opts.exposeConsolidate(consolidate.requires)
  }

  // Map options to local variables
  var def = opts.default;
  var dir = opts.directory || 'layouts';
  var engine = opts.engine;
  var partials = opts.partials;
  var pattern = opts.pattern;
  var rename = opts.rename;

  // Move all unrecognised options to params
  var params = omit(opts, settings);

  /**
   * Main plugin function
   */
  return function(files, metalsmith, done){
    var metadata = metalsmith.metadata();
    var matches = {};

    /**
     * Process any partials and pass them to consolidate as a partials object
     */
    if (partials) {
      if (typeof partials === 'string') {
        params.partials = readPartials(partials, dir, metalsmith);
      } else {
        params.partials = partials;
      }
    }

    /**
     * Stringify files that pass the check, pass to matches
     */
    Object.keys(files).forEach(function(file){
      if (!check(files, file, pattern, def)) {
        return;
      }

      debug('stringifying file: %s', file);
      var data = files[file];
      data.contents = data.contents.toString();
      matches[file] = data;
    });

    /**
     * Render files
     */
    function convert(file, done){
      debug('converting file: %s', file);
      var data = files[file];

      // Deep clone params (by passing 'true')
      var clonedParams = extend(true, {}, params);
      var clone = extend({}, clonedParams, metadata, data);
      var str = metalsmith.path(dir, data.layout || def);
      var render = consolidate[engine];

      // Rename file if necessary
      var fileInfo;
      if (rename) {
        delete files[file];
        fileInfo = path.parse(file);
        file = path.join(fileInfo.dir, fileInfo.name + '.html');
        debug('renamed file to: %s', file);
      }

      render(str, clone, function(err, str){
        if (err) {
          return done(err);
        }

        data.contents = new Buffer(str);
        debug('converted file: %s', file);

        files[file] = data;
        done();
      });
    }

    /**
     * Render all matched files
     */
    each(Object.keys(matches), convert, done);
  };
}

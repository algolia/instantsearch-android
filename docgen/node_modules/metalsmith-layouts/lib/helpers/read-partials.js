/**
 * Dependencies
 */
var path = require('path');
var read = require('fs-readdir-recursive');

/**
 * Expose `readPartials`
 */
module.exports = readPartials;

/**
 * Helper for reading a folder with partials, returns a `partials` object that
 * can be consumed by consolidate.
 *
 * @param {String} partialsPath
 * @param {String} layoutsPath
 * @param {Object} metalsmith
 * @return {Object}
 */
function readPartials(partialsPath, layoutsPath, metalsmith) {
  var partialsAbs = path.isAbsolute(partialsPath) ? partialsPath : path.join(metalsmith.path(), partialsPath);
  var layoutsAbs = path.isAbsolute(layoutsPath) ? layoutsPath : path.join(metalsmith.path(), layoutsPath);
  var files = read(partialsAbs);
  var partials = {};

  // Return early if there are no partials
  if (files.length === 0) {
    return partials;
  }

  // Read and process all partials
  for (var i = 0; i < files.length; i++) {
    var fileInfo = path.parse(files[i]);
    var name = path.join(fileInfo.dir, fileInfo.name);
    var partialAbs = path.join(partialsAbs, name);
    var partialPath = path.relative(layoutsAbs, partialAbs);

    partials[name.replace(/\\/g, '/')] = partialPath;
  }

  return partials;
}

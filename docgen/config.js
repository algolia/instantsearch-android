var algoliaComponents = require('algolia-components');
var fs = require('fs');

import {rootPath} from './path';

const prod = process.env.NODE_ENV === 'production';
const docsDist = process.env.DOCS_DIST;

var content = JSON.parse(fs.readFileSync('./src/data/community_header.json').toString());
var header = algoliaComponents.community_header.html.render(content);

export default {
  docsDist:  docsDist? docsDist :
             prod ? rootPath('docs') : // avoids publishing an `npm start`ed build if running.
             rootPath('docs-preview'),
  publicPath: prod ? '/instantsearch-android/' :
    '/',
  header: header
};

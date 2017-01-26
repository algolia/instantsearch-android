import {rootPath} from './path';

const prod = process.env.NODE_ENV === 'production';
const docsDist = process.env.DOCS_DIST;

export default {
  docsDist:  docsDist? docsDist :
             prod ? rootPath('docs') : // avoids publishing an `npm start`ed build if running.
             rootPath('docs-preview'),
  publicPath: prod ? '/instantsearch-android/' :
    '/',
};

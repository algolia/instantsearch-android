import {rootPath} from './path';

const prod = process.env.NODE_ENV === 'production';

export default {
  docsDist: prod ?
    rootPath('docs-production/react') : // avoids publishing an `npm start`ed build if running.
    rootPath('docs/react'),
  publicPath: prod ?
    'https://community.algolia.com/instantsearch-android/' :
    '/',
};

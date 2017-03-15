import sidebar from './sidebar.js';
import dropdowns from './dropdowns.js';
import move from './mover.js';
var alg = require('algolia-frontend-components/javascripts.js');

const docSearch = {
  apiKey: '52641df1ce4919ba42eb84595f4825c7',
  indexName: 'wordpress_algolia',
  inputSelector: '#searchbox'
}

const header = new alg.communityHeader(docSearch);

const container = document.querySelector('.documentation-container');
const sidebarContainer = document.querySelector('.sidebar');

if (sidebarContainer) {
  sidebar({
    headersContainer: container,
    sidebarContainer,
    headerStartLevel: 2,
  });
}

dropdowns();
move();

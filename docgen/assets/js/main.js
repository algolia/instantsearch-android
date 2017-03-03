import sidebar from './sidebar.js';
import dropdowns from './dropdowns.js';
import move from './mover.js';
import community_header from 'algolia-components';

const docSearch = {
  api_key: '52641df1ce4919ba42eb84595f4825c7',
  index_name: 'wordpress_algolia',
  input_selector: '#searchbox'
}

const communitHeaderInit = community_header.community_header.js.header(docSearch)

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

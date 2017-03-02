import sidebar from './sidebar.js';
import dropdowns from './dropdowns.js';
import move from './mover.js'
import * as communityHeader from 'algolia-components/dist/_community_header.js'

const container = document.querySelector('.documentation-container');
const sidebarContainer = document.querySelector('.sidebar');

if (sidebarContainer) {
  sidebar({
    headersContainer: container,
    sidebarContainer,
    headerStartLevel: 2,
  });
}
console.log(communityHeader)
communityHeader.default();
communityProjects();
dropdowns();
move();

/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId])
/******/ 			return installedModules[moduleId].exports;
/******/
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			exports: {},
/******/ 			id: moduleId,
/******/ 			loaded: false
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.loaded = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "/instantsearch-android/";
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(0);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */
/***/ function(module, exports, __webpack_require__) {

	'use strict';
	
	var _sidebar = __webpack_require__(5);
	
	var _sidebar2 = _interopRequireDefault(_sidebar);
	
	var _dropdowns = __webpack_require__(3);
	
	var _dropdowns2 = _interopRequireDefault(_dropdowns);
	
	var _mover = __webpack_require__(4);
	
	var _mover2 = _interopRequireDefault(_mover);
	
	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
	
	var alg = __webpack_require__(2);
	
	var docSearch = {
	  apiKey: 'fd5e835f5153cad7d5ec0c3595dfa244',
	  indexName: 'instantsearch-android',
	  inputSelector: '#searchbox'
	};
	
	var header = new alg.communityHeader(docSearch);
	
	var container = document.querySelector('.documentation-container');
	var sidebarContainer = document.querySelector('.sidebar');
	
	if (sidebarContainer) {
	  (0, _sidebar2.default)({
	    headersContainer: container,
	    sidebarContainer: sidebarContainer,
	    headerStartLevel: 2
	  });
	}
	
	(0, _dropdowns2.default)();
	(0, _mover2.default)();

/***/ },
/* 1 */
/***/ function(module, exports) {

	/**
	 * Main header function with docsearch
	 * @param  {Object} docSearch config
	 */
	
	class communityHeader {
	
	  constructor(docSearchCredentials, docSearch) {
	
	    this.docSearchCredentials = docSearchCredentials;
	    this.docSearch = docSearch;
	
	    this.menuState = {
	      isOpen: false,
	      isOpenMobile: false
	    }
	
	    this.INIT_VAL = {
	      WIDTH: 490,
	      HEIGHT: 360
	    }
	
	    this.disableTransitionTimeout;
	
	    this.searchIcon = document.querySelector('#search');
	    this.cancelIcon = document.querySelector('#cancel');
	    this.searchContainer = document.querySelector('.algc-search__input').parentNode;
	    this.navRoot = document.querySelector('.algc-dropdownroot');
	    this.dropdownRoot = document.querySelector('.algc-navigation__dropdown-holder');
	    this.navItems = document.querySelectorAll('a[data-enabledropdown="true"]');
	    this.navContainer = document.querySelector('.algc-dropdownroot__dropdowncontainer');
	    this.menuContainer = document.querySelector('.algc-navigation__container');
	    this.navBg = document.querySelector('.algc-dropdownroot__dropdownbg');
	    this.navArrow = document.querySelector('.algc-dropdownroot__dropdownarrow');
	    this.dropDownContainer = document.querySelector('.algc-dropdownroot__dropdowncontainer');
	    this.menuTriggers = document.querySelectorAll('[data-enabledropdown="true"]');
	    this.mobileMenuButton = document.querySelector('.algc-openmobile ');
	    this.mobileMenu = document.querySelector('.algc-mobilemenu');
	    this.subList = document.querySelectorAll('.algc-menu--sublistlink');
	    this.subListHolders = [...this.subList].map(node => node.parentNode);
	    this.menuDropdowns = {};
	
	    [].forEach.call(document.querySelectorAll('[data-dropdown-content]'), (item) => {
	      this.menuDropdowns[item.dataset.dropdownContent] = {
	        parent: item.parentNode,
	        content: item
	      }
	    });
	
	    this.shouldInitDocSearch = this.shouldInitDocSearch.bind(this);
	    this.docSearchInit = this.checkDocSearch(docSearch);
	    this.enableDocSearch = this.verifyDocSearchParams(docSearchCredentials);
	    this.hasDocSearchRendered = document.querySelector('.algc-navigation .algc-search__input--docsearch');
	    this.triggerMenu = this.triggerMenu.bind(this);
	    this.shouldTriggerMenu = this.shouldTriggerMenu.bind(this);
	    this.closeMenu = this.closeMenu.bind(this);
	    this.toggleMobileMenu = this.toggleMobileMenu.bind(this);
	    this.docSearchToggling = this.docSearchToggling.bind(this);
	    this.initDocSearchStrategy = this.initDocSearchStrategy.bind(this);
	    this.openSublist = this.openSublist.bind(this);
	    this.closeSubLists = this.closeSubLists.bind(this);
	    this.bindListeners = this.bindListeners.bind(this);
	
	    this.calculatePosition = this.calculatePosition.bind(this);
	
	    this.verifyDocSearchParams();
	    this.shouldInitDocSearch();
	    this.checkDocSearch();
	    this.initDocSearchStrategy();
	    this.bindListeners();
	  }
	
	  calculatePosition(sourceNode) {
	    const box = sourceNode.getBoundingClientRect();
	    const realWidth = sourceNode.offsetWidth;
	    const realHeight = sourceNode.offsetHeight;
	
	    return {
	      left: box.left,
	      top: box.top,
	      width: box.width,
	      height: box.height,
	      realWidth: realWidth,
	      realHeight: realHeight,
	      center: box.left + box.width / 2
	    }
	  }
	
	  shouldInitDocSearch() {
	    if (!this.enableDocSearch && this.hasDocSearchRendered) {
	      throw new Error('You need to pass docSearch: { apiKey, indexName, inputSelector } to communityHeader function in order to initialise docSearch');
	
	    } else if (this.enableDocSearch && this.hasDocSearchRendered) {
	
	    }
	  }
	
	  checkDocSearch(docSearch = false) {
	
	    if (docSearch) return docSearch;
	
	    else if (typeof docsearch === "function") {
	      return docsearch;
	    }
	  }
	
	  verifyDocSearchParams(docSearchCredentials) {
	    return (docSearchCredentials &&
	      docSearchCredentials.apiKey &&
	      docSearchCredentials.indexName &&
	      docSearchCredentials.inputSelector) ? true : false;
	  }
	
	  triggerMenu(event) {
	
	    const dropdown = event.target.dataset.dropdown;
	    const newTarget = this.menuDropdowns[dropdown].content;
	    const newContent = this.menuDropdowns[dropdown].parent;
	
	    const navItem = this.calculatePosition(event.target);
	    const newTargetCoordinates = this.calculatePosition(newTarget);
	    const menuContainerOffset = this.calculatePosition(this.menuContainer);
	    let leftDistance;
	
	    const scaleFactors = {
	      X: newTargetCoordinates.realWidth / this.INIT_VAL.WIDTH,
	      Y: newTargetCoordinates.realHeight / this.INIT_VAL.HEIGHT
	    }
	
	    leftDistance = (navItem.center - menuContainerOffset.left) + "px";
	
	    if(menuContainerOffset.left < 20){
	      leftDistance = "calc(50% - 36px)"
	    }
	
	    this.navBg.style.cssText = `
	      transform: translateX(${leftDistance}) scale(${scaleFactors.X}, ${scaleFactors.Y})`;
	
	    this.navArrow.style.cssText = `
	      transform: translateX(${leftDistance}) rotate(45deg)`;
	
	    this.dropDownContainer.style.cssText = `
	      transform: translateX(${leftDistance});
	      width: ${newTargetCoordinates.realWidth}px;
	      height: ${newTargetCoordinates.realHeight + 10}px;`;
	
	    this.dropdownRoot.style.pointerEvents = "auto";
	
	    Object.keys(this.menuDropdowns).forEach(key => {
	      if (key === dropdown) {
	        this.menuDropdowns[key].parent.classList.add('active');
	      } else {
	        this.menuDropdowns[key].parent.classList.remove('active');
	      }
	    })
	
	    if (!this.menuState.isOpen) {
	      setTimeout(() => {
	        this.navRoot.className = "algc-dropdownroot activeDropdown";
	      }, 50);
	    }
	
	    window.clearTimeout(this.disableTransitionTimeout);
	    this.menuState.isOpen = true;
	  }
	
	  shouldTriggerMenu(event) {
	    if(this.menuState.isOpen) { 
	      this.triggerMenu(event);
	    } else {
	      this.triggerMenuTimeout = setTimeout(()=>{
	        this.triggerMenu(event);
	      }, 200);
	    }
	  }
	
	  closeMenu(event) {
	    window.clearTimeout(this.triggerMenuTimeout);
	    this.menuState.isOpen = false;
	    this.disableTransitionTimeout = setTimeout(() => {
	      this.dropdownRoot.style.pointerEvents = "none";
	      this.navRoot.className = "algc-dropdownroot notransition"
	    }, 50);
	  }
	
	  toggleMobileMenu(event) {
	    this.mobileMenuButton.classList.toggle('algc-openmobile--open');
	    this.mobileMenu.classList.toggle('algc-mobilemenu--open');
	  }
	
	  // Search
	  docSearchToggling() {
	    this.searchInput = document.querySelector(this.docSearchCredentials.inputSelector);
	    const openSearchInput = () => {
	      this.searchContainer.classList.add('open');
	      this.searchInput.focus();
	    }
	
	    const closeSearchInput = () => {
	      this.searchInput.blur();
	      this.searchContainer.classList.remove('open');
	    }
	
	    const emptySearchInput = () => {
	      if (this.searchInput.value !== '') {
	        this.searchInput.value = '';
	      } else {
	        closeSearchInput();
	      }
	    }
	    this.searchInput.setAttribute('value', '');
	    this.searchIcon.addEventListener('click', openSearchInput);
	    this.cancelIcon.addEventListener('click', emptySearchInput);
	  };
	
	  initDocSearch() {
	    this.docSearchToggling();
	    this.docSearchInit(this.docSearchCredentials);
	  }
	
	  initDocSearchStrategy() {
	    if (this.enableDocSearch && typeof this.docSearchInit === "function") {
	      this.initDocSearch();
	
	    } else if (this.docSearch === "lazy") {
	
	      const docSearchScript = document.createElement('script');
	      docSearchScript.type = 'text/javascript';
	      docSearchScript.async = true;
	      document.body.appendChild(docSearchScript);
	
	      docSearchScript.onload = () => {
	        this.docSearchInit = docsearch;
	        this.initDocSearch();
	      };
	
	      docSearchScript.src = "https://cdn.jsdelivr.net/docsearch.js/2/docsearch.min.js";
	    }
	  }
	
	  openSublist(node) {
	    event.preventDefault();
	    event.stopPropagation();
	    const parent = node.parentNode;
	    this.subListHolders.forEach(holder => {
	      console.log(holder === parent, !parent.classList.contains('open'));
	      if (holder === parent && !parent.classList.contains('open')) {
	        holder.classList.add('open');
	      } else {
	        holder.classList.remove('open');
	      }
	    })
	  }
	
	  closeSubLists(event) {
	    this.subListHolders.forEach(holder => holder.classList.remove('open'));
	  }
	
	  bindListeners() {
	    var that = this;
	    this.subList.forEach(link => {
	      link.addEventListener('click', function(event){
	        that.openSublist(this);
	      });
	    });
	
	    this.menuTriggers.forEach(item => {
	      item.addEventListener('mouseenter', this.shouldTriggerMenu);
	      item.addEventListener('focus', this.triggerMenu);
	    });
	
	    this.navItems.forEach(item => {
	      item.addEventListener('mouseleave', this.closeMenu);
	    });
	
	    this.navContainer.addEventListener('mouseenter', () => {
	      clearTimeout(this.disableTransitionTimeout);
	    });
	
	    this.mobileMenuButton.addEventListener('click', this.toggleMobileMenu);
	    document.addEventListener('click', this.closeSubLists);
	    document.querySelector('.algc-dropdownroot__dropdowncontainer').addEventListener('mouseleave', this.closeMenu);
	  }
	}
	
	module.exports = communityHeader


/***/ },
/* 2 */
/***/ function(module, exports, __webpack_require__) {

	const javascripts = {
	  communityHeader: __webpack_require__(1)
	}
	
	module.exports = javascripts;

/***/ },
/* 3 */
/***/ function(module, exports) {

	'use strict';
	
	Object.defineProperty(exports, "__esModule", {
	  value: true
	});
	function dropdowns() {
	  var openDropdown = document.querySelectorAll('[data-toggle-dropdown]');
	  var otherDropdown = document.querySelectorAll('.simple-dropdown');
	
	  for (var i = 0; i < openDropdown.length; i++) {
	    toggleDropdown(openDropdown[i]);
	  }
	
	  function toggleDropdown(element) {
	    var dropdown = element.getAttribute('data-toggle-dropdown');
	    var theDropdown = document.getElementById(dropdown);
	    element.addEventListener('click', function () {
	      if (!theDropdown.classList.contains('opened')) {
	        for (var _i = 0; _i < otherDropdown.length; _i++) {
	          otherDropdown[_i].classList.remove('opened');
	        }
	
	        theDropdown.classList.add('opened');
	        theDropdown.setAttribute('aria-expanded', 'true');
	        theDropdown.setAttribute('aria-expanded', 'true');
	      } else {
	        theDropdown.classList.remove('opened');
	        theDropdown.setAttribute('aria-expanded', 'false');
	        theDropdown.setAttribute('aria-expanded', 'false');
	      }
	    });
	
	    // When there is a click event
	    // Check if the clicked element is the
	    // dropdown toggler, if not, close the dropdown
	    document.body.addEventListener('click', function (e) {
	      if (e.target !== element) {
	        theDropdown.classList.remove('opened');
	      }
	    });
	  }
	}
	
	exports.default = dropdowns;

/***/ },
/* 4 */
/***/ function(module, exports) {

	'use strict';
	
	Object.defineProperty(exports, "__esModule", {
	  value: true
	});
	function move() {
	  var mover = function mover(args) {
	    var item = args.element;
	    var threesold = item.dataset.threesold;
	    var axis = item.dataset.move;
	    var factor = item.dataset.factor;
	    var xtraTransform = item.dataset.xtraTransform || null;
	    var start = null;
	
	    function moveEl(timestamp, axis) {
	
	      if (!start) start = timestamp;
	      var progress = timestamp - start;
	
	      var value = window.scrollY;
	
	      if (value <= threesold * factor / 2) {
	        if (axis === '-y') {
	          xtraTransform ? item.style.cssText = 'transform: translateY(-' + value / factor * (threesold / factor) + 'px) ' + xtraTransform : item.style.cssText = 'transform: translateY(-' + value / factor * (threesold / factor) + 'px)';
	        } else if (axis === '-x') {
	          xtraTransform ? item.style.cssText = 'transform: translateX(-' + value / factor * (threesold / factor) + 'px) ' + xtraTransform : item.style.cssText = 'transform: translateX(-' + value / factor * (threesold / factor) + 'px)';
	        } else if (axis === '+y') {
	          xtraTransform ? item.style.cssText = 'transform: translateY(' + value / factor * (threesold / factor) + 'px) ' + xtraTransform : item.style.cssText = 'transform: translateY(' + value / factor * (threesold / factor) + 'px)';
	        } else if (axis === '+x') {
	          xtraTransform ? item.style.cssText = 'transform: translateX(' + value / factor * (threesold / factor) + 'px) ' + xtraTransform : item.style.cssText = 'transform: translateX(' + value / factor * (threesold / factor) + 'px)';
	        }
	      }
	
	      if (progress < 2000) {
	        window.requestAnimationFrame(moveEl);
	      }
	    }
	
	    window.addEventListener('scroll', function (e) {
	      window.requestAnimationFrame(function (timestamp) {
	        moveEl(timestamp, axis);
	      });
	    });
	  };
	
	  var animatedElement = document.querySelectorAll('[data-move]');
	  animatedElement.forEach(function (e, s) {
	    mover({
	      element: animatedElement[s]
	    });
	  });
	}
	
	exports.default = move;

/***/ },
/* 5 */
/***/ function(module, exports) {

	'use strict';
	
	Object.defineProperty(exports, "__esModule", {
	  value: true
	});
	exports.default = sidebar;
	
	function _toConsumableArray(arr) { if (Array.isArray(arr)) { for (var i = 0, arr2 = Array(arr.length); i < arr.length; i++) { arr2[i] = arr[i]; } return arr2; } else { return Array.from(arr); } }
	
	function sidebar(options) {
	  var headersContainer = options.headersContainer,
	      sidebarContainer = options.sidebarContainer;
	
	
	  var list = document.createElement('ul');
	  list.classList.add('no-mobile');
	
	  sidebarContainer.appendChild(list);
	  sidebarFollowScroll(sidebarContainer.firstChild);
	  activeLinks(sidebarContainer);
	  scrollSpy(sidebarContainer, headersContainer);
	}
	
	function sidebarFollowScroll(sidebarContainer) {
	  var linksContainer = sidebarContainer.querySelector('ul');
	
	  var _getPositionsKeyEleme = getPositionsKeyElements(sidebarContainer),
	      height = _getPositionsKeyEleme.height,
	      navHeight = _getPositionsKeyEleme.navHeight,
	      footerHeight = _getPositionsKeyEleme.footerHeight,
	      menuHeight = _getPositionsKeyEleme.menuHeight,
	      sidebarTop = _getPositionsKeyEleme.sidebarTop,
	      titleHeight = _getPositionsKeyEleme.titleHeight;
	
	  var positionSidebar = function positionSidebar() {
	    var currentScroll = window.pageYOffset;
	    if (currentScroll > sidebarTop - navHeight) {
	      var fold = height - footerHeight - menuHeight - navHeight;
	      if (currentScroll > fold) {
	        sidebarContainer.style.top = fold - currentScroll + navHeight - 200 + 'px';
	      } else {
	        sidebarContainer.style.top = null;
	      }
	      sidebarContainer.classList.add('fixed');
	      linksContainer.style.maxHeight = 'calc(100vh - ' + (titleHeight + navHeight) + 'px)';
	    } else {
	      sidebarContainer.classList.remove('fixed');
	      linksContainer.style.maxHeight = '';
	    }
	  };
	
	  window.addEventListener('load', positionSidebar);
	  document.addEventListener('DOMContentLoaded', positionSidebar);
	  document.addEventListener('scroll', positionSidebar);
	}
	
	function scrollSpy(sidebarContainer, headersContainer) {
	  var headers = [].concat(_toConsumableArray(headersContainer.querySelectorAll('h2')));
	
	  var setActiveSidebarLink = function setActiveSidebarLink(header) {
	    [].concat(_toConsumableArray(sidebarContainer.querySelectorAll('a'))).forEach(function (item) {
	      var currentHref = item.getAttribute('href');
	      var anchorToFind = '#' + header.getAttribute('id');
	      var isCurrentHeader = currentHref.indexOf(anchorToFind) === currentHref.length - anchorToFind.length;
	      if (isCurrentHeader) {
	        item.classList.add('active');
	      } else {
	        item.classList.remove('active');
	      }
	    });
	  };
	
	  var findActiveSidebarLink = function findActiveSidebarLink() {
	    var highestVisibleHeaders = headers.map(function (header) {
	      return { element: header, rect: header.getBoundingClientRect() };
	    }).filter(function (_ref) {
	      var rect = _ref.rect;
	      return rect.top < window.innerHeight / 3 && rect.bottom < window.innerHeight;
	    }
	    // top element relative viewport position should be at least 1/3 viewport
	    // and element should be in viewport
	    )
	    // then we take the closest to this position as reference
	    .sort(function (header1, header2) {
	      return Math.abs(header1.rect.top) < Math.abs(header2.rect.top) ? -1 : 1;
	    });
	
	    if (headers[0] && highestVisibleHeaders.length === 0) {
	      setActiveSidebarLink(headers[0]);
	      return;
	    }
	
	    if (highestVisibleHeaders[0]) {
	      setActiveSidebarLink(highestVisibleHeaders[0].element);
	    }
	  };
	
	  findActiveSidebarLink();
	  window.addEventListener('load', findActiveSidebarLink);
	  document.addEventListener('DOMContentLoaded', findActiveSidebarLink);
	  document.addEventListener('scroll', findActiveSidebarLink);
	}
	
	// The Following code is used to set active items
	// On the documentation sidebar depending on the
	// clicked item
	function activeLinks(sidebarContainer) {
	  var linksContainer = sidebarContainer.querySelector('ul');
	
	  linksContainer.addEventListener('click', function (e) {
	    if (e.target.tagName === 'A') {
	      [].concat(_toConsumableArray(linksContainer.querySelectorAll('a'))).forEach(function (item) {
	        return item.classList.remove('active');
	      });
	      e.target.classList.add('active');
	    }
	  });
	}
	// The Following function will make the '.sidebar-opener'
	// clickable and it will open/close the sidebar on the
	// documentations
	
	function toggleDocumentationSidebar() {
	  var sidebarNav = document.querySelector('nav.sidebar');
	  var trigger = document.querySelector('.sidebar-opener');
	
	  function init() {
	    var bodySize = document.body.clientWidth;
	    if (bodySize <= 960 && sidebarNav) {
	      trigger.addEventListener('click', function () {
	        sidebarNav.classList.toggle('Showed');
	        trigger.classList.toggle('Showed');
	      });
	    }
	  }
	  init();
	}
	toggleDocumentationSidebar();
	
	window.addEventListener('resize', function () {
	  toggleDocumentationSidebar();
	});
	
	function getPositionsKeyElements($sidebar) {
	  var sidebarBBox = $sidebar.getBoundingClientRect();
	  var title = $sidebar.querySelector('.sidebar-header');
	  var bodyBBox = document.body.getBoundingClientRect();
	  var sidebarTop = sidebarBBox.top - bodyBBox.top;
	  var footer = document.querySelector('#footer');
	  var navigation = document.querySelector('.algc-navigation');
	  var menu = document.querySelector('.sidebar-container');
	  var height = document.querySelector('html').getBoundingClientRect().height;
	  var navHeight = navigation.offsetHeight;
	  var footerHeight = footer.offsetHeight;
	  var menuHeight = menu.offsetHeight;
	  var titleHeight = title.offsetHeight;
	
	  return { sidebarTop: sidebarTop, height: height, navHeight: navHeight, footerHeight: footerHeight, menuHeight: menuHeight, titleHeight: titleHeight };
	}

/***/ }
/******/ ]);
//# sourceMappingURL=main.5aa147faed4a8afcb811-build.js.map
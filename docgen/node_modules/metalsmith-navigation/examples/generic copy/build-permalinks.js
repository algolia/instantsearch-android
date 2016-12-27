'use strict';

var path        = require('path');

var Handlebars  = require('handlebars');

var metalsmith  = require('metalsmith');

var markDown    = require('metalsmith-markdown');
var templates   = require('metalsmith-templates');
var assets      = require('metalsmith-assets');
var permalinks  = require('metalsmith-permalinks');

// var navigation  = require('metalsmith-navigation');
var navigation = require('../../lib/index.js');

var navConfigs = {
    primary:{
        sortBy: 'nav_sort',
        filterProperty: 'nav_groups',
        includeDirs: true

    },
    footer: {
        sortBy: 'nav_sort',
        filterProperty: 'nav_groups',
        includeDirs: true

    }
};

var navSettings = {
    navListProperty: 'navs',
    permalinks: true,
};

var navTask = navigation(navConfigs, navSettings);

var assetsTask = assets({
    source: './assets',
    destination: './assets'
});

var markDownTask = markDown();

var templatesTask = templates({
    engine: 'handlebars'
});

var meta = {
    title: 'Metalcorp',
    description: 'Your full service solution.',
    // used by metalsmith-templates
    partials: {
        breadcrumbs: '_breadcrumbs',

        nav_global : '_nav_global',
        nav_relative : '_nav_relative',
        nav_footer : '_nav_footer',

        nav__children: '_nav__children',
    }
};

var permalinksTask = permalinks();

var relativePathHelper = function(current, target) {
    // normalize and remove starting slash from path
    if(!current || !target){
        return '';
    }
    current = path.normalize(current).slice(0);
    target = path.normalize(target).slice(0);
    current = path.dirname(current);
    return path.relative(current, target).replace(/\\/g, '/');
};

Handlebars.registerHelper('relative_path', relativePathHelper);

var metalsmith = metalsmith(__dirname)
    .clean(true)
    .metadata(meta)
    .use(markDownTask)
    .use(permalinksTask)
    .use(navTask)
    .use(templatesTask)
    .use(assetsTask)
    .build(function(err) {
        if (err) throw err;
    });

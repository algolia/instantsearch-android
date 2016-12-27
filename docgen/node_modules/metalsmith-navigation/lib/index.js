'use strict';
// var debug = require('debug')('metalsmith-navigation');
var merge = require('merge');
var path = require('path');
var lodashSortBy = require('lodash.sortby');

// var util = require('util');
// var inspect = function(obj){
//     console.log(util.inspect(obj, {
//         depth: null,
//         showHidden: true,
//         colors: true
//     }));
// };

var SETTINGS_DEFAULT = {

    /*
    * metadata key all navs will be assigned to metatdata[navListProperty]
    * not set if false
    */
    navListProperty: 'navs',

    /*
    * if true, paths will be transformed to use metalsmith-permalinks
    * metalsmith-permalinks must be run before metalsmith-navigation
    */
    permalinks: false,
};

var NAV_CONFIG_DEFAULT = {

    /*
    * sortby function or property name
    * function example: function(navNode){ return navNode.getValueToSortBy(); }
    */
    sortBy: false,

    /*
    * if true nodes will be sorted by path before sortBy
    * if false the sorting will not be stable unless ALL nodes have a unique sort value
    */
    sortByNameFirst: true,

    /*
    * to be included in this nav config, a file's metadata[filterProperty] must equal as string matching or an array including filterValue
    * ex:
    *   navConfigs = {
    *       footer: {
    *           filterProperty: 'my_nav_group'
    *       }
    *   }
    *   file is only added to footer nav when files[path].my_nav_group == 'footer' OR files[path].my_nav_group.indexOf('footer') !== -1
    */
    filterProperty: false,

    /*
    * if false, nav name (navConfigs key) is used instead
    * ex:
    *   navConfigs = {
    *       footer: {
    *           filterValue: 'footer' // default value used if !navConfigs.footer
    *       }
    *   }
    * if files[path][filterProperty] is a string that equals or an array that contains filterValue it will be included
    */
    filterValue: false,

    /*
    * the file object property that breadcrumb array is assigned to on each file object
    * breadcrumbs not generated or set if false
    * typically only one navConfig should generate breadcrumbs, often one specifically for them
    */
    breadcrumbProperty: 'breadcrumb_path',

    /**
    * each file's full nav path will be assigned to that file's metadata object using the value of propertyPath as the key.
    * only assigned to file metadata objects of files included this navConfig
    * if false will not be assigned to any objects.
    * ex:
    *   navConfigs: {
    *       footer: {
    *           pathProperty: 'my_nav_path'
    *       }
    *   };
    *
    *   // in the template of services/marketing/email.html
    *   // my_nav_path == 'services/marketing/email.html'
    *
    * note: each navConfig can have a different pathProperty as file paths may be differerent in different nav configs.
    */
    pathProperty: 'nav_path',

    /**
    * the file object property that an array of nav child nodes will be assigned to
    */
    childrenProperty: 'nav_children',

    /*
    * if a file and sibling dir have matching names the file will be used as the parent in the nav tree
    * ex: /foo /foo.html
    */
    mergeMatchingFilesAndDirs: true,

    /*
    * if ALL dirs should be included as nav nodes
    */
    includeDirs: false,

    /*
    * optional function to invoke for each node while adding metadata
    * function example: function(navNode, metadata){ navNode.title = "test"; }
    */
    prepareNodeMetadata: false,
};

/**
 * Makes a function to use with lodashSortBy
 * @method makeArraySortByFunc
 * @param {Function|String} sortBy - ( function(item){ return item.sort_property; } )
 *    If string used as the name of the property to sort by (eg. length).
 * @param {Bool} usePermalinks - If true directories with only one child are sorted using that child's sort data.
 * @return {Function}
 */
var makeArraySortByFunc = function(sortBy, usePermalinks){
    usePermalinks = usePermalinks || false;

    if(typeof sortBy !== 'function'){
        var sortByKey = sortBy;

        sortBy = function(node){
            if(node.file){
                return node.file[sortByKey];
            }
        };

    } else {
        var sortByFunc = sortBy;
        sortBy = function(node){
            // first param is the expected metalsmith file object, node object is available as second param
            return sortByFunc(node.file, node);
        };
    }

    if(usePermalinks){
        var _sortBy = sortBy;
        sortBy = function(node){

            // if node is dir and has 1 child sort using that child node instead of the dir
            if(!node.file){
                // filter returns an array of nodes for which the filter function returns true
                var matches = node.children.filter(function(n){
                    return ((n.type === 'file') && (n.name === 'index.html'));
                }) || [];
                if(matches.length === 1) {
                    node = matches[0];
                }
            }
            return _sortBy(node);
        };
    }

    return sortBy;
};

/**
* @method eachTreeNode
* @param {Array} tree - nav node tree
* @param {Function} callback - function(node, parent, depth), if callback returns false the node will be removed
* @return {Array} tree
*/
var eachTreeNode = function(tree, callback){
    var iterate = function(nodes, parent, depth){
        depth = (depth || 0) + 1;
        for(var i = nodes.length - 1; i >= 0; i--){
            var node = nodes[i];
            var result = callback(node, parent, depth);
            if(result === false){
                nodes.splice(i, 1);
            }
            iterate(node.children, node, depth);
        }
    };
    iterate(tree);
    return tree;
};

/**
* Convert list of files to tree structure
* @method pathsToTree
* @param {Array} paths - list of file paths to act on.
* @return Array
*/
var pathsToTree = function(paths){
    var items = [];
    for(var i = 0, l = paths.length; i < l; i++) {
        var path = paths[i];
        var name = path[0];
        var rest = path.slice(1);
        var item = null;
        for(var j = 0, m = items.length; j < m; j++) {
            if(items[j].name === name) {
                item = items[j];
                break;
            }
        }

        if(item === null) {
            item = {
                name: name,
                path: null,
                type: null,
                sort: 0,
                children: []
            };
            items.push(item);
        }
        if(rest.length > 0) {
            item.children.push(rest);
        }
    }
    for(i = 0, l = items.length; i < l; i++) {
        var node = items[i];
        node.children = pathsToTree(node.children);
    }
    return items;
};

/**
* Loop over each node and do the following:
*     set node.path - full path to file.
*     set node.type - 'file' or 'dir'.
*
* @method addNodeMetadata
* @param {Array} tree - tree of nodes to act on
* @param {Array} files - List of files being processed by metalsmith.
* @param {Object} config - A nav config object
* @param {Object} metadata - Metalsmith metadata
* @return {Array} converted array (same obj as tree param)
*/
var addNodeMetadata = function(tree, files, config, metadata){
    var fileList = Object.keys(files);

    eachTreeNode(tree, function(node, parent, depth){
        var path = node.name,
            type = 'dir';

        if(parent){
            path = parent.path + '/' + node.name;
        }

        if(fileList.indexOf(path) !== -1){
            type = 'file';
            var file = files[path];
            node.file = file;
        }

        node.path = path;
        node.type = type;
        node.depth = depth;

        if(config.prepareNodeMetadata){
            config.prepareNodeMetadata(node, metadata);
        }
    });

    return tree;
};

/**
* Sort nodes recursively
* @method sortNodes
* @param {Array} nodes - tree of nodes to act on
* @return {Array} sorted node tree
*/
var sortNodes = function(tree, sortBy){
    tree = lodashSortBy(tree, sortBy);
    for (var i = 0; i < tree.length; i++) {
        var node = tree[i];
        if(node.children){
            node.children = sortNodes(node.children, sortBy);
        }
    }
    return tree;
};

/**
* Find dirs with a sibling with afile name matching the dir name,
* remove the dir and move child nodes from it to the matching file node
* @method mergeFileDirs
* @param {Array} tree - Tree of nodes to act on.
* @return {Array} converted array (same obj as tree param)
*/
var mergeFileDirs = function(tree){

    var merge = function(siblings){
        for (var i = siblings.length - 1; i >= 0; i--){
            var node = siblings[i];
            if(node.type === 'dir'){
                for(var j = siblings.length - 1; j >= 0; j--){
                    var sibling = siblings[j];
                    if(node !== sibling && sibling.type !== 'dir'){
                        // remove file extension
                        var siblingBaseName = sibling.path.replace(/\.[^/.]+$/, "");
                        // if dir name matches node without file extension
                        if(siblingBaseName === node.path){
                            // copy children from dir to file node and mark the dir to be removed

                            // prevent copying duplicates
                            for(var k = node.children.length - 1; k >= 0; k--){
                                var nodeChild = node.children[k];
                                if(sibling.children.indexOf(nodeChild) === -1){
                                    sibling.children.push(nodeChild);
                                }
                            }
                            var index = siblings.indexOf(node);
                            siblings.splice(index, 1);
                            break;
                        }
                    }
                }
            }
        }

        // recurse children
        for(var i = siblings.length - 1; i >= 0; i--){
            var node = siblings[i];
            if(node.children){
                merge(node.children);
            }
        }
    };

    merge(tree);
    return tree;
};

/**
* @method setFileObjectReferences
* @param {Array} tree - nav node tree
* @param {Object} files - Metalsmith files object
* @param {String} pathProperty - the file object property that the nav path is assigned to on each file object, not set if false
* @param {String} navName - the name of the current nav
* @return {Array} tree
*/
var setFileObjectReferences = function(tree, files, pathProperty, childrenProperty, navName){
    eachTreeNode(tree, function(node, parent, depth){
        var path = node.path;
        if(files[path]){
            if(pathProperty){
                node.file[pathProperty] = path;
            }
            if(childrenProperty){
                node.file[childrenProperty] = node.file[childrenProperty] || {};
                node.file[childrenProperty][navName] = node.children;
            }
        }
        node.parent = parent;
    });
    return tree;
};

/**
* @method setBreadcrumbs
* @param {Array} tree - nav node tree
* @param {String} breadcrumbProperty -
* @return {Array} tree
*/
var setBreadcrumbs = function(tree, breadcrumbProperty){
    eachTreeNode(tree, function(node, parent, depth){
        var breadcrumbPath = [];
        if(parent){
            if(parent[breadcrumbProperty]){
                breadcrumbPath = parent[breadcrumbProperty].concat(parent);
            } else {
                breadcrumbPath = [parent];
            }
        }
        node[breadcrumbProperty] = breadcrumbPath;
        if(node.file){
            node.file[breadcrumbProperty] = breadcrumbPath;
        }
    });
    return tree;
};

/**
* @method filterFiles
* @param {Object} files - Metalsmith files object
* @param {String} key - file metadata key
* @param {String} val - file metadata key value to check for
* @return {Object} filtered files object
*/
var filterFiles = function(files, key, val){
    var out = {};
    for(var fileKey in files){
        var file = files[fileKey],
            groups = file[key];

        if(typeof groups === 'string'){
            if(groups === val){
                out[fileKey] = file;
            }
        } else if(groups && groups.indexOf(val) !== -1){
            out[fileKey] = file;
        }
    }
    return out;
};

/**
* @method transformPermalinks
* @param {Array} tree - nav nodes
* @return {Array} tree
*/
var transformPermalinks = function(tree){
    eachTreeNode(tree, function(node, parent, depth){
        if(node.file){
            if(node.depth > 1){
                if(!node.children || !node.children.length){
                    node.path = path.dirname(node.path);
                    node.parent.path = node.path;
                    node.parent.file = node.file;
                    node.parent.add_trailing = true;
                }
                return false;
            }
        }
    });
    return tree;
};

/*
* @method removeDirs
* @param {Array} tree - nav nodes
* @return {Array} tree
*/
var removeDirs = function(tree){
    eachTreeNode(tree, function(node, parent, depth){
        if(node.type === 'dir'){
            return false;
        }
    });
    return tree;
};

/**
* Replace \\ in paths with /
*
* @description windows paths have \\ as separator between folders
* @method replaceDirsSeparator
* @param {Array} tree - nav nodes
* @return {Array} tree
*/
var replaceDirsSeparator = function(navFiles) {
    // string.replace does not support flags in node; use regex flags.
    var winPathSepRegex = /\\/g;
    var newKey;
    var oldKey;
    var updatedFiles = {};

    // Path is used as key in navFiles
    for (oldKey in navFiles) {
        if (!navFiles.hasOwnProperty(oldKey)) {
            continue;
        }
        newKey = oldKey.replace(winPathSepRegex, '/');
        updatedFiles[newKey] = navFiles[oldKey];
    }

    return updatedFiles;
};

/**
* @method getNav
* @param {String} navName - name of this nav config
* @param {Object} config - A nav config object
* @param {Object} navFiles - metalsmith files plugin param
* @param {Object} settings - list of non nav specific plugin settings
* @param {Object} metadata - metalsmith metadata
* @return {Array} nav tree
*/
var getNav = function(navName, config, navFiles, settings, metadata){
    config = merge(NAV_CONFIG_DEFAULT, config);

    var sortByNameFirst             = config.sortByNameFirst,
        sortBy                      = config.sortBy,
        breadcrumbProperty          = config.breadcrumbProperty,
        pathProperty                = config.pathProperty,
        mergeMatchingFilesAndDirs   = config.mergeMatchingFilesAndDirs,
        includeDirs                 = config.includeDirs,
        childrenProperty            = config.childrenProperty,
        filterProperty              = config.filterProperty,
        filterValue                 = config.filterValue || navName;

    settings = settings || {};

    var permalinks = settings.permalinks;

    // filter to files that match the filterProperty and filterValue
    if(filterProperty){
        navFiles = filterFiles(navFiles, filterProperty, filterValue);
    }

    var paths = Object.keys(navFiles).map(function(path) {
        return path.split('/');
    });

    var nodes = pathsToTree(paths);

    nodes = addNodeMetadata(nodes, navFiles, config, metadata);

    if(mergeMatchingFilesAndDirs){
        nodes = mergeFileDirs(nodes);
    }

    if(sortByNameFirst){
        nodes = sortNodes(nodes, function(node){
            return node.path;
        });
    }

    if(sortBy){
        sortBy = makeArraySortByFunc(sortBy, permalinks);
        nodes = sortNodes(nodes, sortBy);
    }

    nodes = setFileObjectReferences(nodes, navFiles, pathProperty, childrenProperty, navName);

    if(breadcrumbProperty){
        nodes = setBreadcrumbs(nodes, breadcrumbProperty);
    }

    if(!includeDirs){
        nodes = removeDirs(nodes);
    }

    return nodes;
};

/**
* @method plugin
* @param {Object} navConfigs - list of nav config objects indexed by name
* @param {Object} navSettings - list of non nav specific plugin settings
* @return {Function}
*/
var plugin = function plugin(navConfigs, navSettings){

    // check for CLI single object param
    if(settings === void 0 && navConfigs && (navConfigs.navConfigs || navConfigs.navSettings)){
        var cliSettings = navConfigs;
        navConfigs = cliSettings.navConfigs;
        navSettings = cliSettings.navSettings;
    }

    navConfigs = navConfigs || {};
    navSettings = navSettings || {};

    var settings = merge(SETTINGS_DEFAULT, navSettings);

    var navListProperty = settings.navListProperty;
    var permalinks      = settings.permalinks;

    return function(files, metalsmith, done){

        var metadata = metalsmith.metadata();

        if(navListProperty){
            metadata[navListProperty] = {};
        }

        // key is the name of the nav
        for(var key in navConfigs){

            var config = navConfigs[key];

            // replace backslash with slash for proper web navigation
            var navFiles = files;
            navFiles = replaceDirsSeparator(navFiles);

            // get processed nav
            var nav = getNav(key, config, navFiles, navSettings, metadata);

            // apply permalinks if set in settings
            if(permalinks){
                nav = transformPermalinks(nav);
            }

            // add to global metadata
            if(navListProperty){
                metadata[navListProperty][key] = nav;
            }
        }
        done();
    };
};

module.exports = plugin;

var assert = require('assert');
var equal = require('assert-dir-equal');
var Metalsmith = require('metalsmith');
var layouts = require('..');

describe('metalsmith-layouts', function(){
  it('should render a basic template', function(done){
    Metalsmith('test/fixtures/basic')
      .use(layouts({ engine: 'swig' }))
      .build(function(err){
        if (err) {
          return done(err);
        }
        equal('test/fixtures/basic/expected', 'test/fixtures/basic/build');
        done();
      });
  });

  it('should accept an engine string', function(done){
    Metalsmith('test/fixtures/basic')
      .use(layouts('swig'))
      .build(function(err){
        if (err) {
          return done(err);
        }
        equal('test/fixtures/basic/expected', 'test/fixtures/basic/build');
        done();
      });
  });

  it('should accept a pattern to match', function(done){
    Metalsmith('test/fixtures/pattern')
      .use(layouts({ engine: 'swig', pattern: '*.md' }))
      .build(function(err){
        if (err) {
          return done(err);
        }
        equal('test/fixtures/pattern/expected', 'test/fixtures/pattern/build');
        done();
      });
  });

  it('should expose consolidate.requires', function(done){
    Metalsmith('test/fixtures/basic')
      .use(layouts({ engine: 'swig', exposeConsolidate: function(requires) {
        assert.deepEqual(requires, require('consolidate').requires);
      }}))
      .build(function(err){
        if (err) {
          return done(err);
        }
        done();
      });
  });

  it('should accept a default template', function(done){
    Metalsmith('test/fixtures/default')
      .use(layouts({ engine: 'swig', pattern: '*.md', default: 'default.html' }))
      .build(function(err){
        if (err) {
          return done(err);
        }
        equal('test/fixtures/pattern/expected', 'test/fixtures/pattern/build');
        done();
      });
  });

  it('should accept a different layouts directory', function(done){
    Metalsmith('test/fixtures/directory')
      .use(layouts({ engine: 'swig', directory: 'templates' }))
      .build(function(err){
        if (err) {
          return done(err);
        }
        equal('test/fixtures/directory/expected', 'test/fixtures/directory/build');
        done();
      });
  });

  it('should mix in global metadata', function(done){
    Metalsmith('test/fixtures/metadata')
      .metadata({ title: 'Global Title' })
      .use(layouts({ engine: 'swig' }))
      .build(function(err){
        if (err) {
          return done(err);
        }
        equal('test/fixtures/metadata/expected', 'test/fixtures/metadata/build');
        done();
      });
  });

  it('should preserve binary files', function(done){
    Metalsmith('test/fixtures/binary')
      .use(layouts({ engine: 'swig' }))
      .build(function(err){
        if (err) {
          return done(err);
        }
        equal('test/fixtures/binary/expected', 'test/fixtures/binary/build');
        done();
      });
  });

  it('should process swig includes', function(done){
    Metalsmith('test/fixtures/include')
      .use(layouts({ engine: 'swig' }))
      .build(function(err){
        if (err) {
          return done(err);
        }
        equal('test/fixtures/include/expected', 'test/fixtures/include/build');
        done();
      });
  });

  it('should be capable of processing partials multiple times', function(done){
    var instance = Metalsmith('test/fixtures/partials-multiple')
      .use(layouts({
        engine: 'handlebars',
        partials: {nav: 'partials/nav'}
      }));

    instance.build(function(err){
      if (err) {
        return done(err);
      }
      instance.build(function(err){
        if (err) {
          return done(err);
        }
        equal('test/fixtures/partials-multiple/expected', 'test/fixtures/partials-multiple/build');
        done();
      });
    });
  });

  it('should find partials in subdirectories correctly', function(done){
    // This test would only fail on Windows if readPartials did not
    // replace backslashes in partial names
    var instance = Metalsmith('test/fixtures/partials-subdirectories')
      .use(layouts({
        engine: 'handlebars',
        partials: 'layouts/partials'
      }));

    instance.build(function(err){
      if (err) {
        return done(err);
      }
      equal('test/fixtures/partials-subdirectories/expected', 'test/fixtures/partials-subdirectories/build');
      done();
    });
  });


  it('should find partials in subdirectories with absolute directory path', function(done){
    var instance = Metalsmith('test/fixtures/partials-subdirectories')
      .use(layouts({
        engine: 'handlebars',
        templates: __dirname + '/fixtures/partials-subdirectories/layouts',
        partials: __dirname + '/fixtures/partials-subdirectories/layouts/partials'
      }));

    instance.build(function(err){
      if (err) {
        return done(err);
      }
      equal('test/fixtures/partials-subdirectories/expected', 'test/fixtures/partials-subdirectories/build');
      done();
    });
  });

  it('should accept a partials option', function(done){
    Metalsmith('test/fixtures/partials-option')
      .use(layouts({
        engine: 'handlebars',
        partials: 'partials'
      }))
      .build(function(err){
        if (err) {
          return done(err);
        }
        equal('test/fixtures/partials-option/expected', 'test/fixtures/partials-option/build');
        done();
      });
  });

  it('should ignore files without a layout', function(done){
    Metalsmith('test/fixtures/ignore')
      .use(layouts('handlebars'))
      .build(function(err){
        if (err) {
          return done(err);
        }
        equal('test/fixtures/ignore/expected', 'test/fixtures/ignore/build');
        done();
      });
  });

  it('should ignore files with layout: false', function(done){
    Metalsmith('test/fixtures/layout-false')
      .use(layouts({engine: 'handlebars', default: 'default.html'}))
      .build(function(err){
        if (err) {
          return done(err);
        }
        equal('test/fixtures/layout-false/expected', 'test/fixtures/layout-false/build');
        done();
      });
  });

  it('should not change file extension by default', function(done) {
    Metalsmith('test/fixtures/rename-option-default')
      .use(layouts({
        engine: 'handlebars'
      }))
      .build(function (err) {
        if (err) return done(err);
        equal('test/fixtures/rename-option-default/expected', 'test/fixtures/rename-option-default/build');
        done();
      });
  });

  it('should change file extension when rename option is set to true', function(done) {
    Metalsmith('test/fixtures/rename-option')
      .use(layouts({
        engine: 'handlebars',
        rename: true
      }))
      .build(function (err) {
        if (err) return done(err);
        equal('test/fixtures/rename-option/expected', 'test/fixtures/rename-option/build');
        done();
      });
  });

  it('should change file extension for nested files when rename option is set to true', function(done) {
    Metalsmith('test/fixtures/rename-option-nested')
      .use(layouts({
        engine: 'handlebars',
        rename: true
      }))
      .build(function (err) {
        if (err) return done(err);
        equal('test/fixtures/rename-option-nested/expected', 'test/fixtures/rename-option-nested/build');
        done();
      });
  });

});

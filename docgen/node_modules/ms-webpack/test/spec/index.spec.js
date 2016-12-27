/* eslint-env mocha */
import Metalsmith from 'metalsmith';
import assert from 'assert';
import {sync as rmSync} from 'rimraf';
import path from 'path';
import {readdirSync} from 'fs';
import metalsmithWebpack, {MetalsmithWebpack} from '../../lib';

const config = {
  context: 'test/fixture/src',
  entry: './js/index.js',
  stats: {
    colors: false,
  },
  output: {
    path: 'test/build',
    publicPath: 'http://example.com/assets/',
    filename: '[name].js',
  },
};

describe('metalsmith-webpack', () => {
  afterEach(() => {
    rmSync('test/build');
  });

  it('should resolve paths', () => {
    const resolvePaths = MetalsmithWebpack.resolvePaths;
    const resolved = resolvePaths(config);

    assert.equal(path.resolve(config.context), resolved.context);
    assert.equal(path.resolve(config.output.path), resolved.output.path);
  });

  it('should output files', (done) => {
    const files = ['main.js'];

    Metalsmith('test')
      .source('fixture/src')
      .destination('build')
      .ignore('js')
      .use(metalsmithWebpack({...config, stats: {json: true}}))
      .build((err) => {
        if (err) return done(err);
        assert.deepEqual(readdirSync('test/build'), files);
        return done();
      });
  });

  it('should set metadata', (done) => {
    const metadata = {
      assets: {
        'main.js': 'http://example.com/assets/main.js',
        'main.map': 'http://example.com/assets/main.js.map',
      },
      assetsByType: {
        js: ['http://example.com/assets/main.js'],
        map: ['http://example.com/assets/main.js.map'],
      },
    };

    const m = Metalsmith('test')
      .source('fixture/src')
      .destination('build')
      .ignore('js')
      .use(metalsmithWebpack({...config, devtool: 'source-maps'}));

    m.build((err) => {
      if (err) return done(err);
      assert.deepEqual(m.metadata().webpack, metadata);
      return done();
    });
  });

  it('should throw error if output path is outside of metalsmith destination',
    (done) => {
      Metalsmith('test')
        .source('fixture/src')
        .destination('foo')
        .ignore('js')
        .use(metalsmithWebpack(config))
        .build((err) => {
          assert.equal(
            err.message,
            'Webpack output path is outside of metalsmith destination.'
          );
          done();
        });
    }
  );
});

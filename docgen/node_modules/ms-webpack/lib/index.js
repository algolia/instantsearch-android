/* eslint-disable no-console */
import webpack from 'webpack';
import path from 'path';
import MemoryFs from 'memory-fs';
import supportsColor from 'supports-color';
import chalk from 'chalk';

export class MetalsmithWebpack {

  constructor(options) {
    const _options = MetalsmithWebpack.resolvePaths(options);

    _options.watch = false;

    this.compiler = webpack(_options);
    this.compiler.outputFileSystem = new MemoryFs();

    this.compiler.plugin('after-emit', (compilation, callback) => {
      this.compiler.purgeInputFileSystem();

      this.compilation = compilation;

      this.setFiles();
      this.setMetadata();

      MetalsmithWebpack.printStats(compilation, _options);

      callback();
    });
  }

  static resolvePaths(options) {
    return {
      ...options,
      context: path.resolve(options.context || process.cwd()),
      output: {
        ...options.output,
        path: path.resolve(options.output.path),
      },
    };
  }

  static printStats(compilation, options) {
    const stats = compilation.getStats();
    const outputOptions = this.getDefaultOutputOptions(options);

    if (outputOptions.json) {
      console.log();
      console.log(JSON.stringify(stats.toJson(outputOptions), null, 2));
      console.log();
    } else if (stats.hash !== this.lastHash) {
      this.lastHash = stats.hash;
      const prefix = `${chalk.magenta('[metalsmith-webpack]')} `;
      const output = stats.toString(outputOptions)
        .split('\n').join(`\n${prefix}`);
      console.log(prefix + output);
      console.log();
    }
  }

  static getDefaultOutputOptions(options) {
    let defaultOutputOptions = {
      colors: supportsColor,
      chunks: true,
      modules: true,
      chunkModules: true,
      reasons: true,
      cached: true,
      cachedAssets: true,
    };

    if (options.stats && !options.stats.json) {
      defaultOutputOptions = {
        ...defaultOutputOptions,
        cached: false,
        cachedAssets: false,
        exclude: ['node_modules', 'bower_components', 'jam', 'components'],
      };
    }

    return {
      ...defaultOutputOptions,
      ...(options.stats || {}),
    };
  }

  setMetadata() {
    const assetsByChunkName = this.compilation.getStats()
      .toJson().assetsByChunkName;
    const publicPath = this.compilation.mainTemplate.getPublicPath({
      hash: this.compilation.hash,
    });
    const assets = Object.keys(assetsByChunkName)
      .reduce((reduced, chunkName) => {
        const chunkAsset = assetsByChunkName[chunkName];

        if (Array.isArray(chunkAsset)) {
          const chunkAssets = chunkAsset.reduce((chunkObj, file) => {
            chunkObj[chunkName + path.extname(file)] = publicPath + file;
            return chunkObj;
          }, {});
          return {
            ...reduced,
            ...chunkAssets,
          };
        }

        reduced[chunkName + path.extname(chunkAsset)] = publicPath + chunkAsset;
        return reduced;
      }, {});

    const assetsByType = Object.keys(this.compilation.assets)
      .reduce((reduced, assetName) => {
        const ext = path.extname(assetName).replace(/^\./, '');
        reduced[ext] = (reduced[ext] || []).concat([publicPath + assetName]);
        return reduced;
      }, {});

    this.metalsmith.metadata().webpack = {assets, assetsByType};
  }

  setFiles() {
    const fs = this.compiler.outputFileSystem;
    Object.keys(this.compilation.assets).forEach((outname) => {
      const asset = this.compilation.assets[outname];

      if (asset.emitted) {
        const fileName = asset.existsAt;
        const name = path.relative(this.metalsmith.destination(), fileName);
        const contents = fs.readFileSync(fileName);
        this.files[name] = {contents, fileName};
      }
    });
  }

  plugin() {
    return (files, metalsmith, done) =>{
      const destination = metalsmith.destination();
      const outputPath = this.compiler.options.output.path;

      if (path.relative(destination, outputPath).startsWith('../')) {
        throw new TypeError(
          'Webpack output path is outside of metalsmith destination.'
        );
      }

      this.metalsmith = metalsmith;
      this.files = files;

      console.log(`\n${chalk.magenta('[metalsmith-webpack]')} compiling...`);

      this.compiler.run(done);
    };
  }
}

export default function(options) {
  return new MetalsmithWebpack(options).plugin();
}

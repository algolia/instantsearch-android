module.exports = {
  context: './src',
  entry: {
    main: './js/main.js'
  },
  output: {
    path: './build/',
    filename: 'js/[name].js',
    chunkFilename: 'js/[name]-chunk[id].js'
  }
};

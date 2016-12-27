module.exports = {
  context: './src',
  entry: {
    main: './js/main.js'
  },
  output: {
    path: './build/',
    filename: 'js/[name].[chunkhash:6].js'
  }
};

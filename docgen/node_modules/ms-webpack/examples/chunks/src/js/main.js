var a = require('./a');
document.body.innerHTML += a;

require.ensure(['./b'], function(require) {
  var b = require('./b');
  var c = require('./c');
  document.body.innerHTML += b + c;
}, 'chunk1');

require.ensure(['./c'], function(require) {
  var d = require('./d');
  document.body.innerHTML += d;
}, 'chunk2');

require.ensure(['./d'], function() {
  document.body.innerHTML += 'o';
});

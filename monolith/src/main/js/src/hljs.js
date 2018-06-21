import 'highlight.js';
import 'highlight.js/lib/languages/javascript.js';
import 'highlight.js/lib/languages/bash.js';

import 'highlight.js/styles/default.css';
import 'highlight.js/styles/dracula.css';
// import 'highlight.js/styles/docco.css';

/*
window.addEventListener('DOMContentLoaded', () => {
  const hljs = require('highlight.js');
  window.hljs = hljs;
  window.hljs.configure({ tabReplace: '  ' });
  window.hljs.initHighlightingOnLoad();
}, false);
*/

window.addEventListener('DOMContentLoaded', () => {
  import(/* webpackChunkName: hljs */ 'highlight.js')
    .then(hljs => {
      hljs.configure({ tabReplace: '  ' });
      hljs.initHighlightingOnLoad();
    });
}, false);

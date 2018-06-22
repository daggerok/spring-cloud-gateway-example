/**
 * 1) find all *.md files paths in ./src/posts directory recursively
 * 2) write list of paths found in reverse order and save as ./src/articles/index.json file
 * 3) this must be done before webpack build
 */

const directoryTree = require('directory-tree');
const getFilesTree = baseDir => directoryTree(baseDir, { extensions: /\.md$/i });
const { resolve } = require('path');
const postsRootDir = resolve(__dirname, 'posts');

const paths = [];
function process(baseDir = postsRootDir) {
  const tree = getFilesTree(baseDir) || {};
  const children = tree.children;
  if (!children) return;
  for (let child of children) {
    if (child.type === 'file') {
      const suffix = child.path.replace(postsRootDir, '');
      paths.push(suffix);
    }
    else process(child.path);
  }
}

process();

const { writeFileSync } = require('fs');
const indexJsPath = resolve(postsRootDir, 'index.js');
const posts = JSON.stringify(paths.reverse(), null, 2);

writeFileSync(
  indexJsPath,
  `module.exports = { posts: ${posts} };`,
  { encoding: 'utf-8' }
);

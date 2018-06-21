module.exports = {
  plugins: {
    'postcss-preset-env': {},
    'autoprefixer': {
      browsers: [
        'last 100 versions', // ;P)
        'safari >= 7',
        'chrome >= 52',
        'firefox >= 48',
      ]
    },
    'precss': {},
  },
};

const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const OptimizeCssPlugin = require('optimize-css-assets-webpack-plugin');
const BabelMinifyPlugin = require('babel-minify-webpack-plugin');
// const UglifyJsPlugin = require('uglifyjs-webpack-plugin');
const CompressionPlugin = require('compression-webpack-plugin');
const BrotliPlugin = require('brotli-webpack-plugin');
const { BaseHrefWebpackPlugin: BaseHrefPlugin } = require('base-href-webpack-plugin');
const ImageMinPlugin = require('imagemin-webpack-plugin').default;

const { resolve } = require('path');
const dist = resolve(__dirname, '..', 'resources', 'public');
const safety = env => env || process.env || {};
const mode = env => safety(env).NODE_ENV || 'production';
const isProduction = env => 'production' === mode(env);
const publicPath = env => safety(env).BASE_HREF || '';

module.exports = env => ({
  node: {
    fs: 'empty',
  },
  optimization: {
    splitChunks: {
      chunks: 'all',
    }
  },
  entry: {
    polyfills: './src/polyfills.js',
    app: './src/index.js',
    highlight: './src/hljs.js',
  },
  mode: mode(env),
  output: {
    filename: '[name]-[contenthash].js',
    chunkFilename: '[name].js',
    path: dist,
    publicPath: publicPath(env),
    jsonpFunction: 'w'
  },
  devServer: {
    contentBase: dist,
    overlay: true,
  },
  resolve: {
    extensions: ['.js', '.jsx', '.json']
  },
  module: {
    rules: [
      {
        test: /\.jsx?$/i,
        use: [
          'babel-loader',
        ],
        include: [
          resolve(__dirname, 'src'),
          resolve(__dirname, 'node_modules/highlight.js'),
        ],
      },
      {
        test: /\.css$/i,
        use: [
          isProduction(env) ? MiniCssExtractPlugin.loader : 'style-loader',
          {
            loader: 'css-loader',
            options: { importLoaders: 1 },
          },
          'postcss-loader',
          {
            loader: 'postcss-loader',
            options: {
              ident: 'postcss',
            },
          },
        ],
        include: [
          resolve(__dirname, 'src'),
          resolve(__dirname, 'node_modules/highlight.js'),
        ]
      },
      {
        test: /\.less$/i,
        use: [
          isProduction(env) ? MiniCssExtractPlugin.loader : 'style-loader',
          {
            loader: 'css-loader',
            options: { importLoaders: 2 },
          },
          {
            loader: 'postcss-loader',
            options: {
              ident: 'postcss',
            },
          },
          'less-loader',
        ],
      },
      {
        test: /\.s(a|c)ss$/i,
        use: [
          isProduction(env) ? MiniCssExtractPlugin.loader : 'style-loader',
          {
            loader: 'css-loader',
            options: { importLoaders: 2 },
          },
          {
            loader: 'postcss-loader',
            options: {
              ident: 'postcss',
            },
          },
          'sass-loader',
        ],
      },
      {
        test: /\.styl$/i,
        use: [
          isProduction(env) ? MiniCssExtractPlugin.loader : 'style-loader',
          {
            loader: 'css-loader',
            options: { importLoaders: 2 },
          },
          'postcss-loader',
          'stylus-loader',
        ],
      },
      {
        test: /\.(ico|jpe?g|png|gif|svg)$/i,
        use: [
          {
            loader: 'file-loader',
            options: {
              name: isProduction(env) ? '[hash].[ext]' : '[path][name].[ext]',
            },
          },
        ],
      },
      /*
      // from: './src/posts/path/to/file.dm'
      // to: './dist/posts/path/to/file.md'
      {
        test: /\.md$/i,
        use: [
          {
            loader: 'string-loader',
          },
          {
            loader: 'file-loader',
            options: {
              name: 'posts/[1]',
              regExp: /\/src\/posts\/(.*)/i,
            }
          },
        ],
      },
      */
      {
        test: /\.md$/i,
        use: [
          { loader: 'html-loader' },
          { loader: 'markdown-loader' },
        ],
      },
    ],
  },
  plugins: [
    new webpack.ProvidePlugin({
      //// _: 'lodash',
      // join: ['lodash', 'join'],
      hljs: 'highlight.js',
    }),
    isProduction(env) ? new BabelMinifyPlugin() : undefined,
    // isProduction(env) ? new UglifyJsPlugin() : undefined,
    isProduction(env) ? new OptimizeCssPlugin() : undefined,
    new MiniCssExtractPlugin({
      filename: '[name]-[contenthash].css',
    }),
    new HtmlWebpackPlugin({
      template: './src/index.html',
      favicon: './src/favicon.ico',
      minify: isProduction(env) ? {
        minifyJS: true,
        minifyCSS: true,
        quoteCharacter: '"',
        decodeEntities: true,
        removeComments: true,
        useShortDoctype: true,
        collapseWhitespace: true
      } : {},
    }),
    new BaseHrefPlugin({
      baseHref: publicPath(env),
    }),
    new webpack.DefinePlugin({
      'process.env': {
        NODE_ENV: JSON.stringify(mode(env)),
        BASE_HREF: JSON.stringify(publicPath(env)),
      },
    }),
    isProduction(env) ? new CompressionPlugin({ algorithm: 'gzip' }) : undefined,
    isProduction(env) ? new BrotliPlugin() : undefined,
    new ImageMinPlugin({
      test: /\.(ico|jpe?g|png|gif|svg)$/i,
      disable: !isProduction(env),
      pngquant: {
        quality: '0-90'
      },
      optipng: {
        optimizationLevel: 9
      }
    })
  ].filter(p => !!p),
});

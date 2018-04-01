const path = require('path'),
    webpack = require('webpack'),
    glob = require("glob"),
    uglify = require('uglifyjs-webpack-plugin'),
    copyWebpackPlugin = require('copy-webpack-plugin'),
    htmlWebpackPlugin = require('html-webpack-plugin'),
    cleanWebpackPlugin = require('clean-webpack-plugin')
;

module.exports = {
    mode: "development",
    entry: glob.sync("./src/*.js"),
    output: {
        path: path.join(__dirname, "build/assets/js"),
        filename: "page.js"
    },
    plugins: [
        //拷贝资源
        new copyWebpackPlugin([{
            from: path.join(__dirname, "static"),
            to: path.join(__dirname, "build"),
            force: true
        }]),
        //清空输出目录
        new cleanWebpackPlugin
        (['build/*',],
            {
                root: __dirname,
                verbose: true,
                dry: false
            }),
        new uglify(),
        new htmlWebpackPlugin(
            {
                filename: path.join(__dirname, "build", "page.html"),
                template: path.join(__dirname, "template", "page.html"),
                inject: false,
                minify: {
                    removeComments: true,
                    collapseWhitespace: true,
                    removeAttributeQuotes: false
                }
            }
        )
    ],
    module: {
        rules: [
            {test: /\.html$/, loader: 'html-loader'}
        ]
    },
    node: {
        fs: "empty"
    }
};
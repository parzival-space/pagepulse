// vue.config.js
module.exports = {
  // https://cli.vuejs.org/config/#devserver-proxy
  devServer: {
      port: 8081,
      proxy: { // TODO: verify that this is actually needed
          '/api': {
              target: 'http://localhost:8080',
              ws: true,
              changeOrigin: true
          }
      }
  }
}
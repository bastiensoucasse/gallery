module.exports = {
    outputDir: 'target/dist',
    assetsDir: 'static',
    devServer: {
        port: 8089,
        proxy: {
            '^/images': {
                target: 'http://localhost:8080',
                ws: true,
                changeOrigin: true
            }
        }
    }
}

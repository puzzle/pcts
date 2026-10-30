const target = process.env.PCTS_BACKEND_URL ?? 'http://localhost:8080'

module.exports = {
  '/api': {
    target,
    secure: false,
    changeOrigin: true,
  },
}

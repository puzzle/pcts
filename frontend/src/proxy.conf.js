const { environment } = require('./environments/environment')

const target = environment.apiUrl || 'http://localhost:8080'

module.exports = {
  '/api': {
    target,
    secure: false,
  },
}

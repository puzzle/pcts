const { environment } = require('./environments/environment')

const target = process.env.API_URL || environment.apiUrl || 'http://localhost:8080'

module.exports = {
  '/api': {
    target,
    secure: false,
  },
}

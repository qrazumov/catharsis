const GATE_WAY = process.env.DEV ? 'http://localhost:8080' : 'http://razumov.tech:8080'
const API_POINT = '/api/v1/'
const API_AUTH = '/api/auth/'
module.exports = {
  GATE_WAY,
  API_POINT,
  API_AUTH
}

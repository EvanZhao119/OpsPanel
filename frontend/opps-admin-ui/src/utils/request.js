/**
 * src/utils/request.js
 * ------------------------------------------------------------
 * Axios instance with:
 *  - Inject access token into requests
 *  - Auto refresh token when 401 happens
 *  - Queue pending requests during token refresh
 * ------------------------------------------------------------
 */

import axios from 'axios'
import {
  getAccessToken,
  getRefreshToken,
  setTokens,
  clearTokens,
} from './auth'

let isRefreshing = false
let refreshQueue = []

/** Add request callback to queue */
function subscribeTokenRefresh(cb) {
  refreshQueue.push(cb)
}

/** Run all queued callbacks once new token is ready */
function onTokenRefreshed(newToken) {
  refreshQueue.forEach(cb => cb(newToken))
  refreshQueue = []
}

const service = axios.create({
  baseURL: import.meta.env.DEV ? '' : (import.meta.env.VITE_API_BASE_URL || ''),
  timeout: 10000
})

/* ------------------------------------------------------------
 * Request Interceptor
 * ------------------------------------------------------------ */
service.interceptors.request.use(
  config => {
    const token = getAccessToken()
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => Promise.reject(error)
)

/* ------------------------------------------------------------
 * Response Interceptor
 * ------------------------------------------------------------ */
service.interceptors.response.use(
  response => response.data,
  async error => {
    const { response, config } = error

    if (!response) return Promise.reject(error)

    /** Access token expired → try refresh */
    if (response.status === 401) {
      const refreshToken = getRefreshToken()
      if (!refreshToken) {
        clearTokens()
        window.location.href = '/login'
        return Promise.reject(error)
      }

      /** Already refreshing → queue this request */
      if (isRefreshing) {
        return new Promise(resolve => {
          subscribeTokenRefresh(newToken => {
            config.headers.Authorization = `Bearer ${newToken}`
            resolve(service(config))
          })
        })
      }

      isRefreshing = true

      try {
        /** call refresh endpoint */
        const refreshRes = await axios.post('/api/auth/refresh', {
          refreshToken: refreshToken
        })

        const newAccess = refreshRes.data?.accessToken
        const newRefresh = refreshRes.data?.refreshToken

        if (!newAccess) throw new Error('Refresh failed')

        // Save tokens
        setTokens({ accessToken: newAccess, refreshToken: newRefresh })

        // Notify queued requests
        onTokenRefreshed(newAccess)

        // Retry failed request
        config.headers.Authorization = `Bearer ${newAccess}`
        return service(config)

      } catch (e) {
        clearTokens()
        window.location.href = '/login'
        return Promise.reject(e)

      } finally {
        isRefreshing = false
      }
    }

    return Promise.reject(error)
  }
)

export default service

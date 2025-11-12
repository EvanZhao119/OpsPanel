/**
 * src/utils/request.js
 * ------------------------------------------------------------
 * Axios wrapper for unified HTTP request handling.
 * Includes:
 *  - Base URL & timeout
 *  - Token injection via request interceptor
 *  - Unified error handling for expired tokens, etc.
 * ------------------------------------------------------------
 */

import axios from 'axios'
import { getToken, removeToken } from './auth'

// Create axios instance
const service = axios.create({
  baseURL: import.meta.env.DEV ? '' : (import.meta.env.VITE_API_BASE_URL || ''), // Backend base URL
  timeout: 10000 // request timeout (ms)
})

// Request interceptor
service.interceptors.request.use(
  (config) => {
    const token = getToken()
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    console.error('[Request Error]', error)
    return Promise.reject(error)
  }
)

// Response interceptor
service.interceptors.response.use(
  (response) => {
    const { data } = response
    // Optional: normalize backend response structure
    if (data && data.code && data.code !== 200) {
      console.warn('[API Warning]', data.message)
    }
    return data
  },
  (error) => {
    console.error('[Response Error]', error)
    if (error.response && error.response.status === 401) {
      // Token expired or invalid
      removeToken()
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export default service

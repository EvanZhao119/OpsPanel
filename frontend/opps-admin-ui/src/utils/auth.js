/**
 * src/utils/auth.js
 * ------------------------------------------------------------
 * Stores Access Token & Refresh Token in localStorage
 * ------------------------------------------------------------
 */

const ACCESS_TOKEN_KEY = 'opspanel_access_token'
const REFRESH_TOKEN_KEY = 'opspanel_refresh_token'

/** Save tokens */
export function setTokens({ accessToken, refreshToken }) {
  if (accessToken) localStorage.setItem(ACCESS_TOKEN_KEY, accessToken)
  if (refreshToken) localStorage.setItem(REFRESH_TOKEN_KEY, refreshToken)
}

/** Get tokens */
export function getAccessToken() {
  return localStorage.getItem(ACCESS_TOKEN_KEY)
}

export function getRefreshToken() {
  return localStorage.getItem(REFRESH_TOKEN_KEY)
}

/** Remove all tokens */
export function clearTokens() {
  localStorage.removeItem(ACCESS_TOKEN_KEY)
  localStorage.removeItem(REFRESH_TOKEN_KEY)
}

/**
 * src/utils/auth.js
 * ------------------------------------------------------------
 * Provides helper methods for managing authentication tokens.
 * Tokens are stored in localStorage for persistence between sessions.
 * ------------------------------------------------------------
 */

const TOKEN_KEY = 'opspanel_token' // You can customize this prefix if needed

/**
 * Save token to localStorage
 * @param {string} token - JWT or access token
 */
export function setToken(token) {
  localStorage.setItem(TOKEN_KEY, token)
}

/**
 * Retrieve token from localStorage
 * @returns {string|null}
 */
export function getToken() {
  return localStorage.getItem(TOKEN_KEY)
}

/**
 * Remove token (on logout or expired)
 */
export function removeToken() {
  localStorage.removeItem(TOKEN_KEY)
}

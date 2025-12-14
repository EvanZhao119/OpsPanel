// src/api/insight/session.js
// -------------------------------------------------
// API for AI insight chat sessions.
// Back-end controller:
//   GET  /api/insight/session/list
//   GET  /api/insight/session/{id}
//   POST /api/insight/session/{id}/close
// -------------------------------------------------

import request from '@/utils/request'

/**
 * List all insight sessions of current user.
 */
export function listInsightSessions() {
  return request({
    url: '/api/insight/session/list',
    method: 'get'
  })
}

/**
 * Get details of a single session.
 * @param {number} id session id
 */
export function getInsightSession(id) {
  return request({
    url: `/api/insight/session/${id}`,
    method: 'get'
  })
}

/**
 * Close a session.
 * @param {number} id session id
 */
export function closeInsightSession(id) {
  return request({
    url: `/api/insight/session/${id}/close`,
    method: 'post'
  })
}

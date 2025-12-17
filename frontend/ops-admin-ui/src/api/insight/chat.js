// src/api/insight/chat.js
// -----------------------------------------------
// API for AI insight chat.
// Back-end controller:
//   POST /insight/chat
// -----------------------------------------------

import request from '@/utils/request'

/**
 * Call AI insight chat.
 * @param {Object} payload
 * @param {number|null} [payload.sessionId] existing session id, or null for new
 * @param {string} [payload.sourceType] source type, e.g. "DASHBOARD"
 * @param {string} [payload.sourceRef] source reference, e.g. dashboard page id
 * @param {string} payload.question user question
 * @param {string} [payload.contextJson] optional JSON string with dashboard context
 */
export function chatInsight(payload) {
  return request({
    url: '/insight/chat',
    method: 'post',
    data: payload
  })
}

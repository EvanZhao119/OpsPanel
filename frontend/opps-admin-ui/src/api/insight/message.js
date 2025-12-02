// src/api/insight/message.js
// -----------------------------------------
// API for insight chat message history.
// Back-end:
//   GET /api/insight/message/listBySession/{id}
// -----------------------------------------

import request from '@/utils/request'

/**
 * List all messages of a session.
 * @param {number} sessionId
 */
export function listInsightMessagesBySession(sessionId) {
  return request({
    url: `/api/insight/message/listBySession/${sessionId}`,
    method: 'get'
  })
}

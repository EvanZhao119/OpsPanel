// src/api/dashboard/data.js
// ------------------------------------------------------
// API for dashboard widget data queries.
// ------------------------------------------------------

import request from '@/utils/request'

/**
 * Query dashboard data by data source code.
 * @param {Object} payload
 * @param {string} payload.dataSource data source code, e.g. "task.totalCount"
 * @param {Object} [payload.params] optional params
 */
export function queryDashboardData(payload) {
  return request({
    url: '/api/dashboard/data/query',
    method: 'post',
    data: payload
  })
}

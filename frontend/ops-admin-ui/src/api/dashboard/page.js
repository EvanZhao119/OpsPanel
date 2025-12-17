// src/api/dashboard/page.js
// ------------------------------------------------------
// API for dashboard page management.
// ------------------------------------------------------

import request from '@/utils/request'

/**
 * Get a dashboard page by id.
 */
export function getDashboardPage(id) {
  return request({
    url: `/dashboard/page/${id}`,
    method: 'get'
  })
}

/**
 * List all dashboard pages.
 */
export function listDashboardPages() {
  return request({
    url: '/dashboard/page/list',
    method: 'get'
  })
}

/**
 * Create a new dashboard page.
 */
export function createDashboardPage(data) {
  return request({
    url: '/dashboard/page',
    method: 'post',
    data
  })
}

/**
 * Update an existing dashboard page.
 */
export function updateDashboardPage(data) {
  return request({
    url: '/dashboard/page',
    method: 'put',
    data
  })
}

/**
 * Soft delete a dashboard page.
 */
export function deleteDashboardPage(id) {
  return request({
    url: `/dashboard/page/${id}`,
    method: 'delete'
  })
}

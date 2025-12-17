// src/api/dashboard/widget.js
// ------------------------------------------------------
// API for dashboard widget management.
// ------------------------------------------------------

import request from '@/utils/request'

/**
 * Get widget by id.
 */
export function getWidget(id) {
  return request({
    url: `/dashboard/widget/${id}`,
    method: 'get'
  })
}

/**
 * List widgets under a given dashboard page.
 */
export function listWidgetsByPage(pageId) {
  return request({
    url: `/dashboard/widget/listByPage/${pageId}`,
    method: 'get'
  })
}

/**
 * Create a new widget.
 */
export function createWidget(data) {
  return request({
    url: '/dashboard/widget',
    method: 'post',
    data
  })
}

/**
 * Update an existing widget.
 */
export function updateWidget(data) {
  return request({
    url: '/dashboard/widget',
    method: 'put',
    data
  })
}

/**
 * Soft delete a widget.
 */
export function deleteWidget(id) {
  return request({
    url: `/dashboard/widget/${id}`,
    method: 'delete'
  })
}

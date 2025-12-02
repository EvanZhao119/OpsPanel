// src/api/dashboard/favorite.js
// ------------------------------------------------------
// API for user dashboard favorites / home page settings.
// ------------------------------------------------------

import request from '@/utils/request'

/**
 * List favorite dashboard pages for current user.
 * Backend currently uses a TODO hard-coded userId = 0L.
 */
export function listFavorites() {
  return request({
    url: '/api/dashboard/favorite/list',
    method: 'get'
  })
}

/**
 * Add a dashboard page to favorites for current user.
 * Not strictly needed for the basic home-page feature,
 * but exposed for future expansion.
 */
export function addFavorite(pageId) {
  return request({
    url: `/api/dashboard/favorite/add/${pageId}`,
    method: 'post'
  })
}

/**
 * Set a dashboard page as home for current user.
 */
export function setHomePage(pageId) {
  return request({
    url: `/api/dashboard/favorite/setHome/${pageId}`,
    method: 'post'
  })
}

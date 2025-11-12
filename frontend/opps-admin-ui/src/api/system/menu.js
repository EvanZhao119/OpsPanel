import request from '@/utils/request'

/**
 * Menu Management API
 */
export function listMenus(params) {
  return request({
    url: '/api/system/menu',
    method: 'get',
    params
  })
}

export function getMenu(id) {
  return request({
    url: `/api/system/menu/${id}`,
    method: 'get'
  })
}

export function createMenu(data) {
  return request({
    url: '/api/system/menu',
    method: 'post',
    data
  })
}

export function updateMenu(data) {
  return request({
    url: '/api/system/menu',
    method: 'put',
    data
  })
}

export function deleteMenu(id) {
  return request({
    url: `/api/system/menu/${id}`,
    method: 'delete'
  })
}

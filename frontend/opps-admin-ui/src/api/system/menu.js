import request from '@/utils/request'

/**
 * Menu Management API
 */
export function listMenus(params) {
  return request({
    url: '/api/system/menu/list',
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
    url: '/api/system/menu/create',
    method: 'post',
    data
  })
}

export function updateMenu(data) {
  return request({
    url: '/api/system/menu/update',
    method: 'post',
    data
  })
}

export function batchDeleteRole(ids) {
  return request({
    url: `/api/system/menu/batch-delete`,
    method: 'post',
    data: ids
  })
}

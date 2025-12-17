import request from '@/utils/request'

/**
 * Menu Management API
 */
export function listMenus(params) {
  return request({
    url: '/system/menu/list',
    method: 'get',
    params
  })
}

export function getMenu(id) {
  return request({
    url: `/system/menu/${id}`,
    method: 'get'
  })
}

export function createMenu(data) {
  return request({
    url: '/system/menu/create',
    method: 'post',
    data
  })
}

export function updateMenu(data) {
  return request({
    url: '/system/menu/update',
    method: 'post',
    data
  })
}

export function batchDeleteRole(ids) {
  return request({
    url: `/system/menu/batch-delete`,
    method: 'post',
    data: ids
  })
}

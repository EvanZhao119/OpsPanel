import request from '@/utils/request'

/**
 * Role Management API
 */
export function listRoles(params) {
  return request({
    url: '/system/role/list',
    method: 'get',
    params
  })
}

export function getRole(id) {
  return request({
    url: `/system/role/${id}`,
    method: 'get'
  })
}

export function createRole(data) {
  return request({
    url: '/system/role/create',
    method: 'post',
    data
  })
}

export function updateRole(data) {
  return request({
    url: '/system/role/update',
    method: 'post',
    data
  })
}

export function batchDeleteRole(ids) {
  return request({
    url: `/system/role/batch-delete`,
    method: 'post',
    data: ids
  })
}


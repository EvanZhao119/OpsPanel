import request from '@/utils/request'

/**
 * Role Management API
 */
export function listRoles(params) {
  return request({
    url: '/api/system/role',
    method: 'get',
    params
  })
}

export function getRole(id) {
  return request({
    url: `/api/system/role/${id}`,
    method: 'get'
  })
}

export function createRole(data) {
  return request({
    url: '/api/system/role',
    method: 'post',
    data
  })
}

export function updateRole(data) {
  return request({
    url: '/api/system/role',
    method: 'put',
    data
  })
}

export function deleteRole(id) {
  return request({
    url: `/api/system/role/${id}`,
    method: 'delete'
  })
}

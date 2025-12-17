import request from '@/utils/request'

/**
 * User-Role Relationship API
 */
export function listUserRoles(params) {
  return request({
    url: '/system/user-role',
    method: 'get',
    params
  })
}

export function assignRoles(data) {
  return request({
    url: '/system/user-role',
    method: 'post',
    data
  })
}

export function removeUserRole(id) {
  return request({
    url: `/system/user-role/${id}`,
    method: 'delete'
  })
}

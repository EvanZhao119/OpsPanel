import request from '@/utils/request'

/**
 * Role-Menu Relationship API
 */
export function listRoleMenus(params) {
  return request({
    url: '/system/role-menu',
    method: 'get',
    params
  })
}

export function assignMenus(data) {
  return request({
    url: '/system/role-menu',
    method: 'post',
    data
  })
}

export function removeRoleMenu(id) {
  return request({
    url: `/system/role-menu/${id}`,
    method: 'delete'
  })
}

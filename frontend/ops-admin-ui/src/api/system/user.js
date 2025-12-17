import request from '@/utils/request'

/**
 * User Management API
 */
export function listUsers(params) {
  return request({
    url: '/system/user/list',
    method: 'get',
    params
  })
}

export function getUser(userId) {
  return request({
    url: `/system/user/${userId}`,
    method: 'get'
  })
}

export function createUser(data) {
  return request({
    url: '/system/user/create',
    method: 'post',
    data
  })
}

export function updateUser(data) {
  return request({
    url: '/system/user/update',
    method: 'post',
    data
  })
}

export function batchDeleteUsers(ids) {
  return request({
    url: `/system/user/batch-delete`,
    method: 'post',
    data: ids
  })
}

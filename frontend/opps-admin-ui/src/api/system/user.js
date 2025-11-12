import request from '@/utils/request'

/**
 * User Management API
 */
export function listUsers(params) {
  return request({
    url: '/api/system/user',
    method: 'get',
    params
  })
}

export function getUser(userId) {
  return request({
    url: `/api/system/user/${userId}`,
    method: 'get'
  })
}

export function createUser(data) {
  return request({
    url: '/api/system/user',
    method: 'post',
    data
  })
}

export function updateUser(data) {
  return request({
    url: '/api/system/user',
    method: 'put',
    data
  })
}

export function deleteUser(userId) {
  return request({
    url: `/api/system/user/${userId}`,
    method: 'delete'
  })
}

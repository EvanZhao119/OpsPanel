// src/api/system/login.js
import request from '@/utils/request'

/**
 * @param {Object} data { username: string, password: string }
 */
export function login(data) {
  return request({
    url: '/api/auth/login', 
    method: 'post',
    data: data
  })
}

export function getUserInfo() {
  return request({
    url: '/api/system/user/info',
    method: 'get'
  })
}

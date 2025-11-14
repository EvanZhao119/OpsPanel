// src/api/system/login.js
import request from '@/utils/request'
import { setTokens } from '@/utils/auth'

/** Login and save tokens */
export async function login(data) {
  const res = await request({
    url: '/api/auth/login',
    method: 'post',
    data
  })

  // Expect backend: { code: 200, data: { accessToken, refreshToken } }
  if (res.code === 200 && res.data) {
    setTokens({
      accessToken: res.data.accessToken,
      refreshToken: res.data.refreshToken
    })
  }

  return res
}

/** Fetch current user info */
export function getUserInfo() {
  return request({
    url: '/api/system/user/info',
    method: 'get'
  })
}

import request from '@/utils/request'

/**
 * Log Management API
 */
export function listLoginLogs(params) {
  return request({
    url: '/system/log/login',
    method: 'get',
    params
  })
}

export function listOperationLogs(params) {
  return request({
    url: '/system/log/operation',
    method: 'get',
    params
  })
}

export function deleteLog(id) {
  return request({
    url: `/system/log/${id}`,
    method: 'delete'
  })
}

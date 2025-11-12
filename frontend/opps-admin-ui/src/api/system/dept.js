import request from '@/utils/request'

/**
 * Department Management API
 */
export function listDepts(params) {
  return request({
    url: '/api/system/dept',
    method: 'get',
    params
  })
}

export function getDept(id) {
  return request({
    url: `/api/system/dept/${id}`,
    method: 'get'
  })
}

export function createDept(data) {
  return request({
    url: '/api/system/dept',
    method: 'post',
    data
  })
}

export function updateDept(data) {
  return request({
    url: '/api/system/dept',
    method: 'put',
    data
  })
}

export function deleteDept(id) {
  return request({
    url: `/api/system/dept/${id}`,
    method: 'delete'
  })
}

import request from '@/utils/request'

/**
 * Department Management API
 */
export function listDepts(params) {
  return request({
    url: '/api/system/dept/list',
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
    url: '/api/system/dept/create',
    method: 'post',
    data
  })
}

export function updateDept(data) {
  return request({
    url: '/api/system/dept/update',
    method: 'post',
    data
  })
}

export function deleteDept(ids) {
  return request({
    url: `/api/system/dept/batch-delete`,
    method: 'post',
    data: ids
  })
}

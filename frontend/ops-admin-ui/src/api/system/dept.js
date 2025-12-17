import request from '@/utils/request'

/**
 * Department Management API
 */
export function listDepts(params) {
  return request({
    url: '/system/dept/list',
    method: 'get',
    params
  })
}

export function getDept(id) {
  return request({
    url: `/system/dept/${id}`,
    method: 'get'
  })
}

export function createDept(data) {
  return request({
    url: '/system/dept/create',
    method: 'post',
    data
  })
}

export function updateDept(data) {
  return request({
    url: '/system/dept/update',
    method: 'post',
    data
  })
}

export function deleteDept(ids) {
  return request({
    url: `/system/dept/batch-delete`,
    method: 'post',
    data: ids
  })
}

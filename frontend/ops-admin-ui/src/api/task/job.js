// src/api/task/job.js
import request from '@/utils/request'

/**
 * Task Job Management API
 */

/**
 * Get job list with optional query filters
 */
export function listJobs(params) {
  return request({
    url: '/task/job/list',
    method: 'get',
    params
  })
}

/**
 * Get details of a single job
 */
export function getJob(id) {
  return request({
    url: `/task/job/${id}`,
    method: 'get'
  })
}

/**
 * Create a new scheduled job
 */
export function createJob(data) {
  return request({
    url: '/task/job',
    method: 'post',
    data
  })
}

/**
 * Update an existing scheduled job
 */
export function updateJob(data) {
  return request({
    url: '/task/job',
    method: 'put',
    data
  })
}

/**
 * Change job status (activate / pause)
 */
export function changeStatus(id, status) {
  return request({
    url: `/task/job/${id}/status/${status}`,
    method: 'put'
  })
}

/**
 * Trigger a job to run once immediately
 */
export function runOnce(id) {
  return request({
    url: `/task/job/${id}/runOnce`,
    method: 'post'
  })
}

/**
 * Delete one or more jobs
 */
export function deleteJobs(ids) {
  return request({
    url: `/task/job/${ids.join(',')}`,
    method: 'delete'
  })
}

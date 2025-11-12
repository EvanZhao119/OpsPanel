/**
 * vite.config.js
 * ------------------------------------------------------------
 * Vite configuration for OpsPanel Admin UI
 * - Vue 3 support
 * - Proxy forwarding for /api → http://localhost:8080
 * - Logs proxy activity in terminal
 * ------------------------------------------------------------
 */

import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd())

  console.log('Vite config loaded')
  console.log('Proxy target:', 'http://localhost:8080')

  return {
    plugins: [vue()],
    resolve: {
      alias: {
        '@': path.resolve(__dirname, './src'),
      },
    },
    server: {
      port: env.VITE_PORT || 5173,
      open: true,
      proxy: {
        '/api': {
          target: 'http://localhost:8080', // backend service port
          changeOrigin: true,
          secure: false,
          ws: true, 
          configure: (proxy, options) => {
            // log all proxy requests and responses
            proxy.on('proxyReq', (proxyReq, req) => {
              const targetUrl = `${options.target}${req.url}`
              console.log(`[VITE PROXY] ${req.method} ${req.url} → ${targetUrl}`)
            })
            proxy.on('proxyRes', (proxyRes, req) => {
              console.log(`[VITE PROXY RES] ${req.method} ${req.url} ← ${proxyRes.statusCode}`)
            })
            proxy.on('error', (err, req, res) => {
              console.error(`[VITE PROXY ERROR] ${req.url}:`, err.message)
            })
          },
        },
      },
    },
    define: {
      __APP_ENV__: JSON.stringify(env.VITE_APP_ENV || 'development'),
    },
  }
})

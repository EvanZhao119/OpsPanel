/**
 * vite.config.js
 * ------------------------------------------------------------
 * Vite configuration for OpsPanel Admin UI
 * - Vue 3 SFC + JSX support
 * - Loads .env files
 * - Full proxy with logging
 * - Aliases
 * ------------------------------------------------------------
 */

import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'
import path from 'path'

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd())

  const backendTarget = env.VITE_BACKEND_URL || 'http://localhost:8080'

  console.log('------------------------------------------------------------')
  console.log(' Vite config loaded')
  console.log(' Backend API Target:', backendTarget)
  console.log('------------------------------------------------------------')

  return {
    plugins: [
      vue(),
      vueJsx() 
    ],

    resolve: {
      alias: {
        '@': path.resolve(__dirname, './src'),
      },
    },

    server: {
      port: env.VITE_PORT ? Number(env.VITE_PORT) : 5173,
      open: true,
      proxy: {
        '/api': {
          target: backendTarget,
          changeOrigin: true,
          secure: false,
          ws: true,

          configure: (proxy, options) => {
            proxy.on('proxyReq', (proxyReq, req) => {
              const fullUrl = `${options.target}${req.url}`
              console.log(`[VITE PROXY] ${req.method} ${req.url} → ${fullUrl}`)
            })

            proxy.on('proxyRes', (proxyRes, req) => {
              console.log(`[VITE PROXY RES] ${req.method} ${req.url} ← ${proxyRes.statusCode}`)
            })

            proxy.on('error', (err, req) => {
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

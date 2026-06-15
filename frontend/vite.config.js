import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  base: '/',
  server: {
    port: 5173,
    proxy: {
      '/ping': {
        target: 'http://backend:8080',
        changeOrigin: true,
      },
      '/metrics': {
        target: 'http://backend:8080',
        changeOrigin: true,
      },
      '/auth': {
        target: 'http://backend:8080',
        changeOrigin: true,
      },
    },
  },
})
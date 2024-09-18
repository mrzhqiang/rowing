import {fileURLToPath, URL} from 'node:url';

import {defineConfig} from 'vite';
import vue from '@vitejs/plugin-vue';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    host: '0.0.0.0',
    port: 6666,
    proxy: {
      // 选项写法
      '/api': {
        target: 'http://127.0.0.1:8888',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    },
    hmr: {
      overlay: false
    },
    // 请确保只添加经常使用的文件，以免在启动时过载 Vite 开发服务器。
    warmup: {
      clientFiles: ['./src/components/*.vue', './src/utils/*.js'],
    },
  },
  optimizeDeps: {
    include: [
      'vue',
      'vue-router',
      'vue-types',
      'element-plus/es/locale/lang/zh-cn',
      'element-plus/es/locale/lang/en',
      '@iconify/iconify',
      '@vueuse/core',
      'axios',
      'qs',
      'echarts',
      'echarts-wordcloud',
      'qrcode',
      '@wangeditor/editor',
      '@wangeditor/editor-for-vue',
      'vue-json-pretty',
      '@zxcvbn-ts/core',
      'dayjs',
      'cropperjs'
    ]
  }
});

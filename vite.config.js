import { fileURLToPath, URL } from "node:url";

import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import vueJsx from "@vitejs/plugin-vue-jsx";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue(), vueJsx()],
  server: {
    proxy: {
      "/api": {
        // target: "http://localhost:8080",
        target: " http://k3isb9.natappfree.cc",
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ""), // 可选，去掉 /api 前缀
      },
    },
  },
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", import.meta.url)),
    },
  },
});

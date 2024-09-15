import vue from "@vitejs/plugin-vue";

//let mode = 'development';
//let mode = 'production';

// https://vitejs.dev/config/
export default ({ mode }) => ({
  plugins: [vue()],
  // 获取 .env 环境配置文件
  env: loadEnv(mode, process.cwd()),
  base: process.env.VITE_BASE_PATH,
  // 用于放置生成的静态资源 (js、css、img、fonts) 的；（项目打包之后，静态资源会放在这个文件夹下）
  //npmassetsDir: "static",
  devServer: {
    proxy: {
      '/navigation': 'http://localhost:9077'
    }
  }
});

function loadEnv(mode, root) {
  const env = {};
  const modePrefix = `VITE_${mode.toUpperCase()}_`;
  for (const key in process.env) {
    //console.log(key)
    if (key.startsWith(modePrefix) || key === "VITE_APP_" + key) {
      env[key] = process.env[key];
    }
  }
  return env;
}

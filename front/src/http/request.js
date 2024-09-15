import axios from 'axios'
//import {networkConfig} from '/src/config/networkConfig'

// 创建axios实例
export default function requestService(config) {
    const service = axios.create({
        // axios中请求配置有baseURL选项，表示请求URL公共部分
        baseURL: import.meta.env.VITE_SERVER_URL,
        // 超时
        timeout: import.meta.env.VITE_REQUEST_TIMEOUT
    })
    // request拦截器
    service.interceptors.request.use(config => {
        console.log("发送请求",config.url,config.data)
        return config
    }, error => {
        console.log(error)
        Promise.reject(error)
    })

    // 响应拦截器
    service.interceptors.response.use(res => {
            console.log("返回:",res.data)
            return res;
        },
        error => {
            return Promise.reject(error)
        }
    )
    return service(config)
}
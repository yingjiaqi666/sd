import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios'

import './assets/main.css'

const app = createApp(App)

// Axios 全局设置：默认携带会话、统一处理 401 重定向到登录
axios.defaults.withCredentials = true
axios.defaults.baseURL = 'http://localhost:8080'
axios.interceptors.response.use(
	(response) => response,
	(error) => {
		const status = error?.response?.status
		if (status === 401) {
			router.push('/login')
		}
		return Promise.reject(error)
	}
)

app.use(router)

app.mount('#app')

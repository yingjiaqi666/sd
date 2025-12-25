import { createRouter, createWebHistory } from 'vue-router';
import axios from 'axios';
import Login from '../views/Login.vue';
import Register from '../views/Register.vue';
import StudentList from '../views/StudentList.vue';
import StudentForm from '../views/StudentForm.vue';

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: Login },
  { path: '/register', component: Register },
  { path: '/students', component: StudentList, meta: { requiresAuth: true } },
  { path: '/students/new', component: StudentForm, meta: { requiresAuth: true } },
  { path: '/students/:id', component: StudentForm, meta: { requiresAuth: true } },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// 全局路由守卫：未登录访问受保护页面时跳转到 /login
router.beforeEach(async (to, from, next) => {
  if (!to.meta?.requiresAuth) {
    return next();
  }
  // 先做本地快速校验：未登录标记则直接跳转
  try {
    const flag = localStorage.getItem('loggedIn');
    if (flag !== 'true') {
      return next('/login');
    }
  } catch {}
  try {
    const res = await axios.get('http://localhost:8080/api/auth/me', {
      withCredentials: true,
      validateStatus: (s) => s === 200,
    });
    if (res && res.status === 200) {
      next();
    } else {
      next('/login');
    }
  } catch (e) {
    next('/login');
  }
});

export default router;

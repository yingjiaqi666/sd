import { createRouter, createWebHistory } from 'vue-router';
import Login from '../views/Login.vue';
import StudentList from '../views/StudentList.vue';
import StudentForm from '../views/StudentForm.vue';

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: Login },
  { path: '/students', component: StudentList },
  { path: '/students/new', component: StudentForm },
  { path: '/students/:id', component: StudentForm },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;

<template>
  <div>
    <h2>Login</h2>
    <form @submit.prevent="login">
      <div>
        <label for="username">Username:</label>
        <input type="text" v-model="username" id="username" />
      </div>
      <div>
        <label for="password">Password:</label>
        <input type="password" v-model="password" id="password" />
      </div>
      <button type="submit">Login</button>
    </form>
    <p v-if="error" class="error">{{ error }}</p>
    <p>
      No account? <router-link to="/register">Register</router-link>
    </p>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      username: '',
      password: '',
      error: '',
    };
  },
  methods: {
    async login() {
      try {
        const params = new URLSearchParams();
        params.append('username', this.username);
        params.append('password', this.password);
        const res = await axios.post('http://localhost:8080/api/auth/login', params, {
          headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
          withCredentials: true,
          // 只把 200 视为成功，302/401 等视为失败
          validateStatus: (status) => status === 200,
        });
        if (res && res.status === 200) {
          try { localStorage.setItem('loggedIn', 'true'); } catch {}
          this.$router.push('/students');
        } else {
          this.error = '用户名或密码错误，请重试';
        }
      } catch (error) {
        console.error('Login failed:', error);
        this.error = '用户名或密码错误，请重试';
      }
    },
  },
};
</script>

<style scoped>
.error {
  color: #c00;
  margin-top: 8px;
}
</style>

<template>
  <div class="fullscreen-page sky-background">
    <div class="form-container">
      <h2 class="form-title">ℝ𝕖𝕘𝕚𝕤𝕥𝕖𝕣 (𝕊𝕥𝕦𝕕𝕖𝕟𝕥)</h2>
      <form @submit.prevent="register" class="form-content">
        <div class="form-group">
          <label for="username">Username:</label>
          <input type="text" v-model="form.username" id="username" class="form-input" />
        </div>
        <div class="form-group">
          <label for="password">Password:</label>
          <input type="password" v-model="form.password" id="password" class="form-input" />
        </div>
        <div class="form-group">
          <label for="name">Name:</label>
          <input type="text" v-model="form.name" id="name" class="form-input" />
        </div>
        <div class="form-group">
          <label for="age">Age:</label>
          <input type="number" v-model.number="form.age" id="age" class="form-input" />
        </div>
        <div class="form-group">
          <label for="major">Major:</label>
          <input type="text" v-model="form.major" id="major" class="form-input" />
        </div>
        <button type="submit" class="btn-primary">Register</button>
      </form>
      <p v-if="error" class="error">{{ error }}</p>
      <p class="link-text">
        已有账号？<router-link to="/login" class="login-link">去登录</router-link>
      </p>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
export default {
  data() {
    return {
      form: { username: '', password: '', name: '', age: 0, major: '' },
      error: '',
    };
  },
  methods: {
    async register() {
      try {
        this.error = '';
        const res = await axios.post('http://localhost:8080/api/auth/register', this.form, {
          withCredentials: true,
          validateStatus: (status) => status === 200,
        });
        if (res && res.status === 200) {
          this.$router.push('/login');
        } else {
          this.error = '注册失败，请重试';
        }
      } catch (e) {
        if (e && e.response && e.response.status === 409) {
          this.error = '用户名已存在，请更换用户名';
        } else {
          this.error = '注册失败，请检查填入信息或稍后重试';
        }
      }
    }
  }
};
</script>

<style scoped>
.sky-background {
  background-image: url('../assets/sky.jpg');
}

.form-title {
  text-align: center;
  margin-bottom: 2rem;
  color: var(--mld-blue-dark);
  font-size: 2rem;
  font-weight: 600;
}

.form-content {
  margin-bottom: 1.5rem;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: var(--mld-blue-dark);
}

.form-input {
  width: 100%;
  padding: 0.75rem;
  border: 2px solid var(--mld-blue-light);
  border-radius: 8px;
  background-color: rgba(255, 255, 255, 0.9);
  color: var(--color-text);
  font-size: 1rem;
  transition: all 0.3s;
}

.form-input:focus {
  outline: none;
  border-color: var(--mld-blue-medium);
  background-color: white;
  box-shadow: 0 0 0 3px rgba(142, 177, 171, 0.1);
}

.btn-primary {
  width: 100%;
  padding: 0.875rem;
  background: linear-gradient(135deg, var(--mld-blue-medium) 0%, var(--mld-blue-dark) 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
  margin-top: 0.5rem;
  box-shadow: 0 4px 12px rgba(142, 177, 171, 0.3);
}

.btn-primary:hover {
  background: linear-gradient(135deg, var(--mld-blue-light) 0%, var(--mld-blue-medium) 100%);
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(142, 177, 171, 0.4);
}

.btn-primary:active {
  transform: translateY(0);
  box-shadow: 0 2px 8px rgba(142, 177, 171, 0.3);
}

.error {
  color: #C94B4B;
  margin: 1rem 0;
  padding: 0.75rem;
  background-color: rgba(201, 75, 75, 0.1);
  border-radius: 8px;
  border-left: 4px solid #C94B4B;
  text-align: center;
  font-size: 0.9rem;
}

.link-text {
  text-align: center;
  color: var(--mld-gray-dark);
  margin-top: 1.5rem;
  font-size: 0.95rem;
}

.login-link {
  color: var(--mld-blue-dark);
  font-weight: 600;
  text-decoration: none;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  transition: all 0.3s;
}

.login-link:hover {
  background-color: rgba(160, 199, 198, 0.15);
  text-decoration: none;
}

/* 深色主题适配 */
@media (prefers-color-scheme: dark) {
  .form-input {
    background-color: rgba(255, 255, 255, 0.1);
    color: var(--vt-c-text-dark-1);
    border-color: var(--mld-blue-medium);
  }

  .form-input:focus {
    background-color: rgba(255, 255, 255, 0.15);
    border-color: var(--mld-blue-light);
  }

  .login-link {
    color: var(--mld-blue-accent);
  }
}
</style>
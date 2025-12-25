<template>
  <div>
    <h2>Register (Student)</h2>
    <form @submit.prevent="register">
      <div>
        <label for="username">Username:</label>
        <input type="text" v-model="form.username" id="username" />
      </div>
      <div>
        <label for="password">Password:</label>
        <input type="password" v-model="form.password" id="password" />
      </div>
      <div>
        <label for="name">Name:</label>
        <input type="text" v-model="form.name" id="name" />
      </div>
      <div>
        <label for="age">Age:</label>
        <input type="number" v-model.number="form.age" id="age" />
      </div>
      <div>
        <label for="major">Major:</label>
        <input type="text" v-model="form.major" id="major" />
      </div>
      <button type="submit">Register</button>
    </form>
    <p v-if="error" class="error">{{ error }}</p>
    <p>
      已有账号？<router-link to="/login">去登录</router-link>
    </p>
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
.error {
  color: #c00;
  margin-top: 8px;
}
</style>
<template>
  <div>
    <h2>Student Management</h2>
    <div style="margin-bottom: 12px; display: flex; gap: 8px; align-items: center;">
      <router-link v-if="isAdmin" to="/students/new">Add Student</router-link>
      <span style="flex: 1 1 auto"></span>
      <button @click="logout">Logout</button>
    </div>
    <div v-if="cacheInfo" class="cache-info" style="margin-bottom: 12px; font-size: 14px; color: #555;">
      <strong>User:</strong> {{ cacheInfo.username }}
      &nbsp;|&nbsp;
      <strong>Roles:</strong> {{ (cacheInfo.roles || []).join(', ') }}
      &nbsp;|&nbsp;
      <strong>Login:</strong> {{ formatLoginTime(cacheInfo.loginTime) }}
    </div>
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th>Age</th>
          <th>Major</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="student in students" :key="student.id">
          <td>{{ student.id }}</td>
          <td>{{ student.name }}</td>
          <td>{{ student.age }}</td>
          <td>{{ student.major }}</td>
          <td>
            <router-link :to="`/students/${student.id}`">Edit</router-link>
            <button @click="deleteStudent(student.id)">Delete</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      students: [],
      isAdmin: false,
      cacheInfo: null,
    };
  },
  async created() {
    try {
      const response = await axios.get('http://localhost:8080/api/students', { withCredentials: true });
      this.students = response.data;
      const me = await axios.get('http://localhost:8080/api/auth/me', { withCredentials: true });
      this.isAdmin = (me.data && me.data.role === 'ADMIN');
      const cache = await axios.get('http://localhost:8080/api/auth/cache/me', { withCredentials: true, validateStatus: (s) => s === 200 });
      if (cache && cache.status === 200) {
        this.cacheInfo = cache.data;
      }
    } catch (error) {
      console.error('Failed to fetch students:', error);
    }
  },
  methods: {
    formatLoginTime(d) {
      try { return new Date(d).toLocaleString(); } catch { return d; }
    },
    async logout() {
      try {
        await axios.post('http://localhost:8080/api/auth/logout', null, { withCredentials: true });
        try { localStorage.removeItem('loggedIn'); } catch {}
        this.$router.push('/login');
      } catch (error) {
        console.error('Logout failed:', error);
      }
    },
    async deleteStudent(id) {
      try {
        await axios.delete(`http://localhost:8080/api/students/${id}`, { withCredentials: true });
        this.students = this.students.filter(student => student.id !== id);
      } catch (error) {
        console.error('Failed to delete student:', error);
      }
    },
  },
};
</script>

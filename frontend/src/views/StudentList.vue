<template>
  <div class="fullscreen-page flowers-background">
    <div class="table-container">
      <h2 class="page-title">𝕊𝕥𝕦𝕕𝕖𝕟𝕥 𝕄𝕒𝕟𝕒𝕘𝕖𝕞𝕖𝕟𝕥</h2>

      <div class="header-actions">
        <router-link v-if="isAdmin" to="/students/new" class="btn-add">
          <span class="icon">＋</span> Add Student
        </router-link>
        <span class="spacer"></span>
        <button @click="logout" class="btn-logout">
          <span class="icon">🚪</span> Logout
        </button>
      </div>

      <div v-if="cacheInfo" class="cache-info">
        <div class="info-item">
          <span class="info-label">User:</span>
          <span class="info-value">{{ cacheInfo.username }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">Roles:</span>
          <span class="info-value">{{ (cacheInfo.roles || []).join(', ') }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">Login:</span>
          <span class="info-value">{{ formatLoginTime(cacheInfo.loginTime) }}</span>
        </div>
      </div>

      <div class="table-wrapper">
        <table class="students-table">
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
            <td class="action-buttons">
              <router-link :to="`/students/${student.id}`" class="btn-edit">
                <span class="icon">🪶</span> Edit
              </router-link>
              <button @click="deleteStudent(student.id)" class="btn-delete">
                <span class="icon">🗑️</span> Delete
              </button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
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

<style scoped>
.flowers-background {
  background-image: url('../assets/flowers.jpg');
}

.page-title {
  text-align: center;
  margin-bottom: 1.5rem;
  color: var(--mld-pink-dark);
  font-size: 2rem;
  font-weight: 600;
}

.header-actions {
  display: flex;
  align-items: center;
  margin-bottom: 1.5rem;
  padding-bottom: 1.5rem;
  border-bottom: 2px solid var(--mld-pink-light);
}

.btn-add {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.625rem 1.25rem;
  background: linear-gradient(135deg, var(--mld-pink-medium) 0%, var(--mld-pink-dark) 100%);
  color: white;
  text-decoration: none;
  border-radius: 8px;
  font-weight: 600;
  transition: all 0.3s;
  box-shadow: 0 4px 12px rgba(203, 204, 199, 0.3);
}

.btn-add:hover {
  background: linear-gradient(135deg, var(--mld-pink-light) 0%, var(--mld-pink-medium) 100%);
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(203, 204, 199, 0.4);
}

.btn-logout {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.625rem 1.25rem;
  background: linear-gradient(135deg, #A0A0A0 0%, #7A7A7A 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 4px 12px rgba(160, 160, 160, 0.3);
}

.btn-logout:hover {
  background: linear-gradient(135deg, #B0B0B0 0%, #8A8A8A 100%);
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(160, 160, 160, 0.4);
}

.icon {
  font-size: 0.9em;
}

.spacer {
  flex: 1;
}

.cache-info {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  margin-bottom: 1.5rem;
  padding: 1rem;
  background-color: rgba(220, 205, 202, 0.15);
  border-radius: 8px;
  font-size: 0.9rem;
  border: 1px solid var(--mld-pink-light);
}

.info-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 0.75rem;
  background-color: white;
  border-radius: 6px;
  border: 1px solid var(--mld-pink-light);
}

.info-label {
  font-weight: 600;
  color: var(--mld-pink-dark);
}

.info-value {
  color: var(--color-text);
}

.table-wrapper {
  overflow-x: auto;
  border-radius: 8px;
  border: 1px solid var(--mld-pink-light);
}

.students-table {
  width: 100%;
  border-collapse: collapse;
  background-color: white;
}

.students-table th {
  background: linear-gradient(135deg, var(--mld-pink-light) 0%, var(--mld-pink-medium) 100%);
  padding: 1rem;
  text-align: left;
  font-weight: 600;
  color: white;
  border-bottom: 3px solid var(--mld-pink-dark);
  white-space: nowrap;
}

.students-table td {
  padding: 0.875rem 1rem;
  border-bottom: 1px solid var(--mld-pink-light);
}

.students-table tr:last-child td {
  border-bottom: none;
}

.students-table tr:hover {
  background-color: rgba(220, 205, 202, 0.1);
}

.action-buttons {
  display: flex;
  gap: 0.5rem;
}

.btn-edit {
  display: inline-flex;
  align-items: center;
  gap: 0.25rem;
  padding: 0.375rem 0.75rem;
  background: linear-gradient(135deg, var(--mld-pink-medium) 0%, var(--mld-pink-dark) 100%);
  color: white;
  text-decoration: none;
  border-radius: 6px;
  font-size: 0.85rem;
  font-weight: 500;
  transition: all 0.3s;
}

.btn-edit:hover {
  background: linear-gradient(135deg, var(--mld-pink-light) 0%, var(--mld-pink-medium) 100%);
  transform: translateY(-1px);
}

.btn-delete {
  display: inline-flex;
  align-items: center;
  gap: 0.25rem;
  padding: 0.375rem 0.75rem;
  background: linear-gradient(135deg, #C94B4B 0%, #A63B3B 100%);
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 0.85rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-delete:hover {
  background: linear-gradient(135deg, #D95B5B 0%, #B64B4B 100%);
  transform: translateY(-1px);
}

/* 深色主题适配 */
@media (prefers-color-scheme: dark) {
  .cache-info {
    background-color: rgba(220, 205, 202, 0.1);
  }

  .info-item {
    background-color: var(--color-background-soft);
    color: var(--vt-c-text-dark-1);
  }

  .students-table {
    background-color: var(--color-background-soft);
  }

  .students-table th {
    background: linear-gradient(135deg, var(--mld-pink-dark) 0%, #9C8A86 100%);
  }

  .students-table td {
    color: var(--vt-c-text-dark-1);
  }
}
</style>
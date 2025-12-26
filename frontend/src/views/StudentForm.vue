<template>
  <div class="fullscreen-page flowers-background">
    <div class="form-container">
      <h2 class="form-title">{{ isEdit ? 'Edit Student' : 'Add Student' }}</h2>
      <form @submit.prevent="saveStudent" class="form-content">
        <div class="form-group">
          <label for="name">Name:</label>
          <input type="text" v-model="student.name" id="name" class="form-input" />
        </div>
        <div class="form-group">
          <label for="age">Age:</label>
          <input type="number" v-model="student.age" id="age" class="form-input" />
        </div>
        <div class="form-group">
          <label for="major">Major:</label>
          <input type="text" v-model="student.major" id="major" class="form-input" />
        </div>
        <div class="form-actions">
          <button type="submit" class="btn-primary">{{ isEdit ? 'Update' : 'Save' }}</button>
          <router-link to="/students" class="btn-cancel">Cancel</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      student: {
        name: '',
        age: 0,
        major: '',
      },
      isEdit: false,
    };
  },
  async created() {
    if (this.$route.params.id) {
      this.isEdit = true;
      try {
        const response = await axios.get(`http://localhost:8080/api/students/${this.$route.params.id}`, { withCredentials: true });
        this.student = response.data;
      } catch (error) {
        console.error('Failed to fetch student:', error);
      }
    }
  },
  methods: {
    async saveStudent() {
      try {
        if (this.isEdit) {
          await axios.put(`http://localhost:8080/api/students/${this.$route.params.id}`, this.student, { withCredentials: true });
        } else {
          await axios.post('http://localhost:8080/api/students', this.student, { withCredentials: true });
        }
        this.$router.push('/students');
      } catch (error) {
        console.error('Failed to save student:', error);
      }
    },
  },
};
</script>

<style scoped>
.flowers-background {
  background-image: url('../assets/flowers.jpg');
}

.form-title {
  text-align: center;
  margin-bottom: 2rem;
  color: var(--mld-pink-dark);
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
  color: var(--mld-pink-dark);
}

.form-input {
  width: 100%;
  padding: 0.75rem;
  border: 2px solid var(--mld-pink-light);
  border-radius: 8px;
  background-color: rgba(255, 255, 255, 0.9);
  color: var(--color-text);
  font-size: 1rem;
  transition: all 0.3s;
}

.form-input:focus {
  outline: none;
  border-color: var(--mld-pink-medium);
  background-color: white;
  box-shadow: 0 0 0 3px rgba(203, 204, 199, 0.1);
}

.form-actions {
  display: flex;
  gap: 1rem;
  margin-top: 2rem;
}

.btn-primary {
  flex: 1;
  padding: 0.875rem;
  background: linear-gradient(135deg, var(--mld-pink-medium) 0%, var(--mld-pink-dark) 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 4px 12px rgba(203, 204, 199, 0.3);
}

.btn-primary:hover {
  background: linear-gradient(135deg, var(--mld-pink-light) 0%, var(--mld-pink-medium) 100%);
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(203, 204, 199, 0.4);
}

.btn-primary:active {
  transform: translateY(0);
  box-shadow: 0 2px 8px rgba(203, 204, 199, 0.3);
}

.btn-cancel {
  flex: 1;
  padding: 0.875rem;
  background: linear-gradient(135deg, #A0A0A0 0%, #7A7A7A 100%);
  color: white;
  text-decoration: none;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 600;
  text-align: center;
  transition: all 0.3s;
  box-shadow: 0 4px 12px rgba(160, 160, 160, 0.3);
}

.btn-cancel:hover {
  background: linear-gradient(135deg, #B0B0B0 0%, #8A8A8A 100%);
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(160, 160, 160, 0.4);
}

.btn-cancel:active {
  transform: translateY(0);
  box-shadow: 0 2px 8px rgba(160, 160, 160, 0.3);
}

/* 深色主题适配 */
@media (prefers-color-scheme: dark) {
  .form-input {
    background-color: rgba(255, 255, 255, 0.1);
    color: var(--vt-c-text-dark-1);
    border-color: var(--mld-pink-medium);
  }

  .form-input:focus {
    background-color: rgba(255, 255, 255, 0.15);
    border-color: var(--mld-pink-light);
  }
}
</style>
<template>
  <div>
    <h2>{{ isEdit ? 'Edit Student' : 'Add Student' }}</h2>
    <form @submit.prevent="saveStudent">
      <div>
        <label for="name">Name:</label>
        <input type="text" v-model="student.name" id="name" />
      </div>
      <div>
        <label for="age">Age:</label>
        <input type="number" v-model="student.age" id="age" />
      </div>
      <div>
        <label for="major">Major:</label>
        <input type="text" v-model="student.major" id="major" />
      </div>
      <button type="submit">Save</button>
    </form>
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
        const response = await axios.get(`http://localhost:8080/api/students/${this.$route.params.id}`);
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
          await axios.put(`http://localhost:8080/api/students/${this.$route.params.id}`, this.student);
        } else {
          await axios.post('http://localhost:8080/api/students', this.student);
        }
        this.$router.push('/students');
      } catch (error) {
        console.error('Failed to save student:', error);
      }
    },
  },
};
</script>

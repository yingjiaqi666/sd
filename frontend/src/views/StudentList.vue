<template>
  <div>
    <h2>Student Management</h2>
    <router-link to="/students/new">Add Student</router-link>
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
    };
  },
  async created() {
    try {
      const response = await axios.get('http://localhost:8080/api/students');
      this.students = response.data;
    } catch (error) {
      console.error('Failed to fetch students:', error);
    }
  },
  methods: {
    async deleteStudent(id) {
      try {
        await axios.delete(`http://localhost:8080/api/students/${id}`);
        this.students = this.students.filter(student => student.id !== id);
      } catch (error) {
        console.error('Failed to delete student:', error);
      }
    },
  },
};
</script>

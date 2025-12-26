<template>
  <div class="fullscreen-page sky-background">
    <div class="form-container" ref="formContainer">
      <h2 class="form-title">𝕃𝕠𝕘𝕚𝕟</h2>
      <form @submit.prevent="login" class="form-content">
        <div class="form-group">
          <label for="username">Username:</label>
          <input type="text" v-model="username" id="username" class="form-input" />
        </div>
        <div class="form-group">
          <label for="password">Password:</label>
          <input type="password" v-model="password" id="password" class="form-input" />
        </div>
        <button type="submit" class="btn-primary" @click="createStarEffect" ref="loginBtn">
          Login
        </button>
      </form>
      <p v-if="error" class="error">{{ error }}</p>
      <p class="link-text">
        No account? <router-link to="/register" class="register-link">Register</router-link>
      </p>
    </div>
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
    async login(event) {
      try {
        const params = new URLSearchParams();
        params.append('username', this.username);
        params.append('password', this.password);
        const res = await axios.post('http://localhost:8080/api/auth/login', params, {
          headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
          withCredentials: true,
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

    createStarEffect(event) {
      const button = event.currentTarget;
      const formContainer = this.$refs.formContainer;

      // 获取按钮在容器中的位置
      const buttonRect = button.getBoundingClientRect();
      const containerRect = formContainer.getBoundingClientRect();

      // 计算相对位置
      const centerX = buttonRect.left + buttonRect.width / 2 - containerRect.left;
      const centerY = buttonRect.top + buttonRect.height / 2 - containerRect.top;

      // 创建流星粒子
      const particles = 25; // 粒子数量
      const colors = [
        '#FFFFFF', // 白色
        '#FFFAF0', // 花白色
        '#F0F8FF', // 爱丽丝蓝
        '#FFFACD'  // 柠檬绸
      ];

      for (let i = 0; i < particles; i++) {
        this.createParticle(
            centerX,
            centerY,
            colors[Math.floor(Math.random() * colors.length)],
            formContainer
        );
      }
    },

    createParticle(x, y, color, container) {
      const particle = document.createElement('div');
      particle.classList.add('particle');

      // 随机大小 (2-8px)
      const size = Math.random() * 6 + 2;
      particle.style.width = `${size}px`;
      particle.style.height = `${size}px`;
      particle.style.backgroundColor = color;

      // 随机位置偏移
      const offsetX = (Math.random() - 0.5) * 100;
      const offsetY = (Math.random() - 0.5) * 100;

      // 随机旋转
      const rotation = Math.random() * 360;

      // 设置初始位置
      particle.style.left = `${x}px`;
      particle.style.top = `${y}px`;
      particle.style.transform = `rotate(${rotation}deg)`;

      // 添加到容器
      container.appendChild(particle);

      // 动画参数
      const animation = particle.animate([
        {
          transform: `translate(0, 0) rotate(${rotation}deg) scale(1)`,
          opacity: 1,
          boxShadow: `0 0 ${size * 2}px ${color}`
        },
        {
          transform: `translate(${offsetX}px, ${offsetY}px) rotate(${rotation + 180}deg) scale(0)`,
          opacity: 0,
          boxShadow: `0 0 ${size * 5}px ${color}`
        }
      ], {
        duration: Math.random() * 800 + 800, // 800-1600ms
        easing: 'cubic-bezier(0.1, 0.8, 0.3, 1)',
        fill: 'forwards'
      });

      // 动画结束后移除元素
      animation.onfinish = () => {
        particle.remove();
      };
    }
  },
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
  position: relative;
  overflow: hidden;
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

/* 添加按钮点击时的脉冲效果 */
.btn-primary::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 5px;
  height: 5px;
  background: rgba(255, 255, 255, 0.5);
  opacity: 0;
  border-radius: 100%;
  transform: scale(1, 1) translate(-50%, -50%);
  transform-origin: 50% 50%;
}

.btn-primary:focus:not(:active)::after {
  animation: ripple 1s ease-out;
}

@keyframes ripple {
  0% {
    transform: scale(0, 0);
    opacity: 0.5;
  }
  100% {
    transform: scale(40, 40);
    opacity: 0;
  }
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

.register-link {
  color: var(--mld-blue-dark);
  font-weight: 600;
  text-decoration: none;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  transition: all 0.3s;
}

.register-link:hover {
  background-color: rgba(160, 199, 198, 0.15);
  text-decoration: none;
}

/* 流星粒子样式 */
.particle {
  position: absolute;
  border-radius: 50%;
  pointer-events: none;
  z-index: 1000;
  filter: blur(0.5px);
  will-change: transform, opacity;
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

  .register-link {
    color: var(--mld-blue-accent);
  }
}
</style>
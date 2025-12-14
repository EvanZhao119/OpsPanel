<template>
  <div>
    <div class="login-container">
      <div class="login-box">
        <h2 class="title">OpsPanel Admin</h2>

        <form @submit.prevent="handleLogin">
          <input
            v-model="username"
            placeholder="Username"
            required
            class="input"
          />
          <input
            type="password"
            v-model="password"
            placeholder="Password"
            required
            class="input"
          />
          <button type="submit" class="login-btn">Login</button>
        </form>

        <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
      </div>
    </div>
  </div>
</template>


<script setup>
/**
 * Login page for OpsPanel Admin
 * ------------------------------------------------------------
 * This page handles:
 *  - Collecting username and password
 *  - Calling the API /api/auth/login
 *  - Saving JWT token in localStorage
 *  - Redirecting users to dashboard after login
 * ------------------------------------------------------------
 */

import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { setTokens } from '@/utils/auth'
import { login } from '@/api/system/login'  
import { useUserStore } from '@/store/user'
import { getUserInfo } from '@/api/system/login'


const router = useRouter()
const userStore = useUserStore()

// Form fields
const username = ref('')
const password = ref('')

// Error message shown under the form
const errorMessage = ref('')

/**
 * Handle login form submission
 * ------------------------------------------------------------
 * Sends login request and validates response.
 * On success:
 *   - Save token
 *   - Redirect to dashboard
 * On failure:
 *   - Display error message
 * ------------------------------------------------------------
 */
const handleLogin = async () => {
  console.log("HANDLE LOGIN CALLED")

  errorMessage.value = ''

  console.log('[Login Attempt] username=', username.value)

  try {
    // Call backend API
    const res = await login({
      username: username.value,
      password: password.value
    })

    console.log('[Login Response] ', res)

    // Backend must return { code: 200, data: { token: 'xxx' } }
    if (res && res.code === 200 && res.data?.accessToken) {
      setTokens({ accessToken: res.data.accessToken, refreshToken: res.data.refreshToken })
      console.log('[Login Success] Token saved.')

      const userRes = await getUserInfo()
      if (userRes.code === 200) {
        userStore.setUserInfo(userRes.data)
      }
      router.push('/dashboard')
    } else {
      errorMessage.value = res?.msg || 'Invalid username or password.'
      console.warn('[Login Failed]', res)
    }
  } catch (err) {
    console.error('[Login Error]', err)
    errorMessage.value = err?.message || 'Network error.'
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: #f0f2f5;
}

.login-box {
  background: #fff;
  padding: 40px;
  border-radius: 8px;
  box-shadow: 0 0 12px rgba(0, 0, 0, 0.1);
  width: 320px;
  text-align: center;
}

.title {
  margin-bottom: 20px;
}

.input {
  width: 100%;
  padding: 10px;
  margin-bottom: 16px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.login-btn {
  width: 100%;
  padding: 10px;
  background: #409eff;
  border: none;
  color: white;
  border-radius: 4px;
  cursor: pointer;
}

.error {
  color: #ff4d4f;
  margin-top: 10px;
}
</style>

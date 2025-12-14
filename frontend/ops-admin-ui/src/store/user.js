import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: null,
    username: '',
    userInfo: null
  }),

  actions: {
    setUser(token, username) {
      this.token = token
      this.username = username
    },

    setUserInfo(info) {
      this.userInfo = info
      this.username = info?.username || ''   
    },

    logout() {
      this.token = null
      this.username = ''
      this.userInfo = null
    }
  }
})

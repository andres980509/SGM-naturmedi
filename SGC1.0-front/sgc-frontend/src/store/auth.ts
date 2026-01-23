import { defineStore } from "pinia"
import axios from "axios"

export const useAuthStore = defineStore("auth", {
  state: () => ({
    token: localStorage.getItem("token") as string | null,
    rol: localStorage.getItem("rol") as string | null,
    username: localStorage.getItem("username") as string | null,
  }),

  actions: {
    async login(username: string, password: string) {
      const res = await axios.post("/api2/login", {
        username,
        password,
      })

      this.token = res.data.token
      this.rol = res.data.rol
      this.username = res.data.username

      localStorage.setItem("token", this.token!)
      localStorage.setItem("rol", this.rol!)
      localStorage.setItem("username", this.username!)
    },

    logout() {
      this.token = null
      this.rol = null
      this.username = null

      localStorage.removeItem("token")
      localStorage.removeItem("rol")
      localStorage.removeItem("username")
    },
  },
})

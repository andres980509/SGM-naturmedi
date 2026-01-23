import axios from "axios"

const apiPriv = axios.create({
  baseURL: "/api",
})

apiPriv.interceptors.request.use((config) => {
  const token = localStorage.getItem("token")
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

apiPriv.interceptors.response.use(
  (res) => res,
  (err) => {
    if (err.response?.status === 401) {
      localStorage.clear()
      window.location.href = "/login"
    }
    return Promise.reject(err)
  }
)

export default apiPriv

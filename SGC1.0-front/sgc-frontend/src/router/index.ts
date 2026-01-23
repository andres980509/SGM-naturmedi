import { createRouter, createWebHistory } from "vue-router"
import Login from "../views/Login.vue"
import Dashboard from "../views/Dashboard.vue"
import CrearUsuario from "../views/CrearUsuario.vue"
import Usuarios from "../views/Usuarios.vue"
import MainLayout from "../layouts/MainLayout.vue"
import { useAuthStore } from "../store/auth"

const router = createRouter({
  history: createWebHistory(),
  routes: [
    // 🔓 LOGIN (sin layout)
    {
      path: "/login",
      component: Login,
    },

    // 🔐 TODAS LAS VISTAS CON HEADER
    {
      path: "/",
      component: MainLayout,
      meta: { requiresAuth: true },
      children: [
        {
          path: "dashboard",
          component: Dashboard,
        },

        // 📋 LISTADO DE USUARIOS (ADMIN)
        {
          path: "usuarios",
          component: Usuarios,
          meta: { rol: "ADMIN" },
        },

        // ➕ CREAR USUARIO (ADMIN)
        {
          path: "usuarios/crear",
          component: CrearUsuario,
          meta: { rol: "ADMIN" },
        },
      ],
    },

    // 🔁 REDIRECT INICIAL
    {
      path: "/",
      redirect: "/login",
    },
  ],
})

router.beforeEach((to) => {
  const auth = useAuthStore()

  // 1️⃣ Ruta protegida sin token
  if (to.meta.requiresAuth && !auth.token) {
    return "/login"
  }

  // 2️⃣ Ruta con rol específico
  if (to.meta.rol && auth.rol !== to.meta.rol) {
    return "/dashboard"
  }

  // 3️⃣ Evitar volver al login si ya está logueado
  if (to.path === "/login" && auth.token) {
    return "/dashboard"
  }
})

export default router

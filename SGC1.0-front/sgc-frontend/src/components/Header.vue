<template>
  <header class="flex justify-between items-center bg-gray-800 text-white px-6 py-3">
    <!-- Logo -->
    <h1 class="font-bold text-lg">SGC</h1>

    <!-- Menú derecho -->
    <nav class="relative">
      <!-- Botón usuario -->
      <button
        @click="toggleUserMenu"
        class="flex items-center gap-2 hover:text-gray-300"
      >
        <UserCircleIcon class="w-7 h-7 text-gray-300" />
        <span class="font-medium">{{ auth.username }}</span>
        <ChevronDownIcon class="w-4 h-4" />
      </button>

      <!-- Dropdown usuario -->
      <div
        v-if="showUserMenu"
        class="absolute right-0 mt-2 w-60 bg-white text-black rounded shadow-lg p-1 z-50"
      >
        <!-- 🔐 SEGURIDAD (solo ADMIN) -->
        <div v-if="esAdmin">
          <button
            @click="toggleSeguridad"
            class="flex items-center justify-between w-full px-4 py-2 hover:bg-gray-100 rounded"
          >
            <div class="flex items-center gap-2">
              <ShieldCheckIcon class="w-4 h-4" />
              <span class="font-medium">Seguridad</span>
            </div>
            <ChevronDownIcon
              class="w-4 h-4 transition-transform"
              :class="{ 'rotate-180': showSeguridad }"
            />
          </button>

          <!-- Submenú seguridad -->
          <div v-if="showSeguridad" class="ml-6 mt-1 space-y-1">
            <button
              @click="irUsuarios"
              class="flex items-center gap-2 w-full px-4 py-2 hover:bg-gray-100 rounded"
            >
              <UsersIcon class="w-4 h-4" />
              Listar usuarios
            </button>

            <button
              @click="irCrearUsuario"
              class="flex items-center gap-2 w-full px-4 py-2 hover:bg-gray-100 rounded"
            >
              <UserPlusIcon class="w-4 h-4" />
              Crear usuario
            </button>
          </div>

          <hr class="my-1" />
        </div>

        <!-- 🚪 Cerrar sesión (mismo nivel que Seguridad) -->
        <button
          @click="salir"
          class="flex items-center gap-2 w-full px-4 py-2 text-red-600 hover:bg-gray-100 rounded"
        >
          <ArrowRightOnRectangleIcon class="w-4 h-4" />
          Cerrar sesión
        </button>
      </div>
    </nav>
  </header>
</template>

<script setup lang="ts">
import { ref, computed } from "vue"
import { useAuthStore } from "../store/auth"
import { useRouter } from "vue-router"

// Icons
import {
  UserCircleIcon,
  ArrowRightOnRectangleIcon,
  ShieldCheckIcon,
  ChevronDownIcon,
  UserPlusIcon,
  UsersIcon,
} from "@heroicons/vue/24/outline"

const auth = useAuthStore()
const router = useRouter()

const esAdmin = computed(() => auth.rol === "ADMIN")

// estados del menú
const showUserMenu = ref(false)
const showSeguridad = ref(false)

const toggleUserMenu = () => {
  showUserMenu.value = !showUserMenu.value
  if (!showUserMenu.value) {
    showSeguridad.value = false
  }
}

const toggleSeguridad = () => {
  showSeguridad.value = !showSeguridad.value
}

// navegación
const irCrearUsuario = () => {
  router.push("/usuarios/crear")
}

const irUsuarios = () => {
  router.push("/usuarios")
}

const salir = () => {
  auth.logout()
  router.push("/login")
}
</script>

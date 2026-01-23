<script setup lang="ts">
import { ref } from "vue"
import { useAuthStore } from "../store/auth"
import { useRouter } from "vue-router"

const username = ref("")
const password = ref("")
const error = ref("")

const auth = useAuthStore()
const router = useRouter()

const login = async () => {
  error.value = ""
  try {
    await auth.login(username.value, password.value)
    router.push("/dashboard")
  } catch (e) {
    error.value = "Usuario o contraseña incorrectos"
  }
}
</script>

<template>
  <div class="min-h-screen flex items-center justify-center bg-slate-900">
    <form
      @submit.prevent="login"
      class="bg-white p-8 rounded-lg shadow-md w-96 space-y-4"
    >
      <h1 class="text-2xl font-bold text-center">SGC Login</h1>

      <input
        v-model="username"
        placeholder="Usuario"
        class="w-full border p-2 rounded"
      />

      <input
        v-model="password"
        type="password"
        placeholder="Contraseña"
        class="w-full border p-2 rounded"
      />

      <button
        class="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700"
      >
        Ingresar
      </button>

      <p v-if="error" class="text-red-600 text-sm text-center">
        {{ error }}
      </p>
    </form>
  </div>
</template>

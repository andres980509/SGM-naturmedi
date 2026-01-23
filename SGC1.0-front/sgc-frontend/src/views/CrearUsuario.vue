<template>
  <div class="max-w-3xl mx-auto p-6">
    <h1 class="text-2xl font-bold mb-6">Crear Usuario</h1>

    <form @submit.prevent="guardar" class="grid grid-cols-2 gap-4">

      <!-- DATOS PERSONALES -->
      <input v-model="form.cedula" placeholder="Cédula" class="input" />
      <input v-model="form.nombres" placeholder="Nombres" class="input" />
      <input v-model="form.apellidos" placeholder="Apellidos" class="input" />
      <input v-model="form.email" placeholder="Email" class="input" />
      <input v-model="form.telefono" placeholder="Teléfono" class="input" />
      <input v-model="form.direccion" placeholder="Dirección" class="input col-span-2" />

      <!-- USUARIO SISTEMA -->
      <input v-model="form.username" placeholder="Usuario" class="input" />
      <input v-model="form.password" type="password" placeholder="Contraseña" class="input" />

      <select v-model="form.rol" class="input">
        <option value="">Seleccione rol</option>
        <option value="ADMIN">ADMIN</option>
        <option value="VENDEDOR">VENDEDOR</option>
      </select>

      <div class="col-span-2 flex justify-end gap-2 mt-4">
        <button type="submit" class="btn-primary">Guardar</button>
      </div>

    </form>
  </div>
</template>

<script setup lang="ts">
import { reactive } from "vue"
import api from "../services/api_priv"
import { useRouter } from "vue-router"

const router = useRouter()

const form = reactive({
  cedula: "",
  nombres: "",
  apellidos: "",
  email: "",
  telefono: "",
  direccion: "",
  username: "",
  password: "",
  rol: "",
})

const guardar = async () => {
  try {
    await api.post("/usuarios", form)
    alert("Usuario creado correctamente")
    router.push("/dashboard")
  } catch (err: any) {
    alert(err.response?.data || "Error al crear usuario")
  }
}
</script>

<style scoped>
.input {
  @apply border p-2 rounded w-full;
}

.btn-primary {
  @apply bg-blue-600 text-white px-4 py-2 rounded;
}
</style>

<template>


  <div class="max-w-6xl mx-auto p-6">
    <h1 class="text-2xl font-bold mb-4">Usuarios</h1>

    <table class="w-full border">
      <thead class="bg-gray-100">
        <tr>
          <th class="th">Usuario</th>
          <th class="th">Nombre</th>
          <th class="th">Rol</th>
          <th class="th">Estado</th>
          <th class="th">Acciones</th>
        </tr>
      </thead>

      <tbody>
        <tr v-for="u in usuarios" :key="u.id" class="border-t">
          <td class="td">{{ u.username }}</td>
          <td class="td">
            {{ u.datos.nombres }} {{ u.datos.apellidos }}
          </td>
          <td class="td">{{ u.rol }}</td>

          <!-- 🔘 ACTIVO -->
          <td class="td">
            <label class="flex items-center gap-2">
              <input
                type="checkbox"
                :checked="u.activo"
                @change="toggleEstado(u)"
              />
              <span>{{ u.activo ? "Activo" : "Inactivo" }}</span>
            </label>
          </td>

          <!-- 🔑 ACCIONES -->
          <td class="td flex gap-2">
            <button
              class="btn-warning"
              @click="abrirReset(u)"
            >
              Reset clave
            </button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- MODAL RESET -->
    <ResetPasswordModal
      v-if="usuarioReset"
      :usuario="usuarioReset"
      @cerrar="usuarioReset = null"
      @confirmar="resetPassword"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue"
import api from "../services/api_priv"
import Header from "../components/Header.vue"
import ResetPasswordModal from "../components/ResetPasswordModal.vue"

const usuarios = ref<any[]>([])
const usuarioReset = ref<any | null>(null)

const cargar = async () => {
  const res = await api.get("/usuarios")
  usuarios.value = res.data
}

onMounted(cargar)

// 🔘 activar / desactivar
const toggleEstado = async (u: any) => {
  await api.patch(`/usuarios/${u.id}/estado`, {
    activo: !u.activo,
  })
  u.activo = !u.activo
}

// 🔑 reset
const abrirReset = (u: any) => {
  usuarioReset.value = u
}

const resetPassword = async (id: number, nueva: string) => {
  await api.patch(`/usuarios/${id}/reset-password`, {
    password: nueva,
  })
  alert("Contraseña restablecida")
  usuarioReset.value = null
}
</script>

<style scoped>
.th {
  @apply p-2 text-left;
}
.td {
  @apply p-2;
}
.btn-warning {
  @apply bg-yellow-500 text-white px-3 py-1 rounded;
}
</style>

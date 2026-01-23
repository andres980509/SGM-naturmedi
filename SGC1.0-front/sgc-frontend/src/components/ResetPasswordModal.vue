<template>
  <div class="fixed inset-0 bg-black/50 flex items-center justify-center">
    <div class="bg-white p-6 rounded w-96">
      <h2 class="text-lg font-bold mb-4">
        Reset contraseña: {{ usuario.username }}
      </h2>

      <input
        v-model="password"
        type="password"
        placeholder="Nueva contraseña"
        class="border p-2 w-full mb-4"
      />

      <div class="flex justify-end gap-2">
        <button @click="$emit('cerrar')" class="btn-secondary">
          Cancelar
        </button>
        <button @click="confirmar" class="btn-primary">
          Guardar
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue"

const props = defineProps<{ usuario: any }>()
const emit = defineEmits(["cerrar", "confirmar"])

const password = ref("")

const confirmar = () => {
  if (!password.value) {
    alert("Ingrese contraseña")
    return
  }
  emit("confirmar", props.usuario.id, password.value)
}
</script>

<style scoped>
.btn-primary {
  @apply bg-blue-600 text-white px-4 py-2 rounded;
}
.btn-secondary {
  @apply bg-gray-400 text-white px-4 py-2 rounded;
}
</style>

<template>
  <Header />
  <main class="min-h-screen bg-gray-100">
    <router-view />
  </main>
</template>

<script setup lang="ts">
import Header from "../components/Header.vue"
</script>

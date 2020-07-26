<template>
    <span>{{ roleName }}</span>
</template>

<script>
export default {
  name: 'CellRendererRole',
  computed: {
    roleName () {
      if (this.params.value==='A') {
        return "Admin"
      } else if (this.params.value==='M') {
        return "Manager"
      } else if (this.params.value==='O') {
        return "Agent"
      }
    }
  }
}
</script>

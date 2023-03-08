<template>
    <v-app-bar
    app
    color="primarygrey darken-4"
    dark>
      <v-app-bar-title class="pink--text text--accent-4 font-weight-bold">ToDo</v-app-bar-title>
      <v-spacer></v-spacer>

      <CreateTodo @notify="forwarde"/>
    </v-app-bar>
</template>

<script>
import CreateTodo from './createTodoCard/CreateTodo.vue'

export default {
  name: 'NavBar',
  components: {
    CreateTodo
  },
  methods: {
    forwarde (message) {
      this.$emit('notify', message)
    }
  }
}
</script>
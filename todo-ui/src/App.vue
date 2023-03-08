<template>
  <v-app id="app">
    <VerifyUser @notify="setNotificationMessage"/>
    <NavBar @notify="setNotificationMessage"/>
    <v-main>
      <h2 class="grey--text text--lighten-1 text-center my-10" v-text="'Hello ' + this.$store.state.username"></h2>

      <ToolBar />
      <TodoContainer v-bind:todos="getTasks"/>

      <v-snackbar
        v-model="snackbar"
        :timeout="5000"
        :multi-line="true"
        color="red--text text--darken-3 text-center">
            {{ getNotification }}
        </v-snackbar>
    </v-main>
  </v-app>
</template>

<script>
import NavBar from './components/NavBar.vue'
import ToolBar from './components/ToolBar.vue'
import TodoContainer from './components/TodoContainer.vue'
import VerifyUser from './components/VerifyUser.vue'

export default {
  name: 'App',
  components: {
    NavBar,
    ToolBar,
    TodoContainer,
    VerifyUser
  },
  methods: {
    setNotificationMessage () {
      this.snackbar = true
    }
  },
  computed: {
    getTasks () {
      var sortedTasks = [...this.$store.state.todos]
      if (this.$store.state.order.localeCompare('n') === 0) {
        return sortedTasks.sort((task1, task2) =>
          (task1.duedate < task2.duedate) ? -1 : (task1.duedate > task2.duedate) ? 1 : 0
        )
      }
      if (this.$store.state.order.localeCompare('f') === 0) {
        return sortedTasks.sort((task1, task2) =>
          (task1.duedate > task2.duedate) ? -1 : (task1.duedate < task2.duedate) ? 1 : 0
        )
      }
      return sortedTasks
    },
    getNotification () {
      return this.$store.state.notification
    },
    display () {
      return this.snackbar
    }
  },
  data () {
    return {
      snackbar: false
    }
  }
}
</script>

<style>
  #app{
    background-color: #404040;
  }

  .toolbar{
    width: 80%;
    margin: 0 10%;
  }
</style>
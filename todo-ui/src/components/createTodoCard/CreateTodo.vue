<template>
  <div>
    <v-row justify="end">
        <v-col cols="auto" class="ma-0">
            <v-dialog
            transition="dialog-top-transition"
            max-width="600">
              <template v-slot:activator="{ on, attrs }">
                  <v-btn
                  color="accent"
                  elevation="2"
                  outlined
                  plain
                  rounded
                  text
                  v-bind="attrs"
                  v-on="on">
                      Create Task
                  </v-btn>
              </template>

              <template v-slot:default="dialog">
                  <v-card>
                    <v-toolbar
                    color="primary"
                    dark>
                      Create a new Todo
                    </v-toolbar>

                    <TaskForm @todoCreated="sendTodoSaveRequest" v-bind:status="loading"/>

                    <v-card-actions class="justify-end">
                      <v-btn id="createTodoCloseButton"
                      text
                      color="red"
                      @click="dialog.value = false">
                        Close
                      </v-btn>
                    </v-card-actions>
                  </v-card>
              </template>
            </v-dialog>
        </v-col>
    </v-row>
  </div>
</template>

<script>
import axios from 'axios'
import TaskForm from './TaskForm.vue'
import serverConfig from '@/user-config.js'

export default {
  name: 'CreateTodo',
  components: {
    TaskForm
  },
  methods: {
    closeCreateToDoForm () {
      document.getElementById('createTodoCloseButton').click()
    },
    handlePostResponse (response) {
      this.loading = true
      this.closeCreateToDoForm()
      let appJson = {
        title: this.json.title,
        note: this.json.note,
        creationdate: this.json.creationDate,
        duedate: this.json.dueDate
      }
      this.$store.commit('addTodo', appJson)
      this.$emit('notify', response.data)
      this.$store.commit('setNotificationMessage', response.data)
    },
    handlePostError (error) {
      if (error.response.status === 402) {
        this.loading = true
        this.closeCreateToDoForm()
        this.$emit('notify', error.response.data)
        this.$store.commit('setNotificationMessage', error.response.data)
      } else {
        this.loading = false
      }
    },
    sendTodoSaveRequest (object) {
      this.json = {
        title: object.title,
        note: object.note,
        creationDate: object.creationDate,
        dueDate: object.dueDate
      }

      const request = serverConfig.api.baseUrl + serverConfig.routerOptions.main + "/" + this.$store.state.username
      axios.post(request, this.getTaskData, {
        headers: {
          'Content-Type': 'application/json',
        }
      })
      .then(this.handlePostResponse)
      .catch(this.handlePostError)
    }
  },
  computed: {
    getTaskData() {
      return this.json;
    }
  },
  data () {
    return {
      json: '',
      loading: true
    }
  }
}
</script>
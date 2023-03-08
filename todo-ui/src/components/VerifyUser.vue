<template>
    <v-row justify="end">
        <v-col cols="auto" class="ma-0">
            <v-dialog
            transition="dialog-center-transition"
            value="true"
            fullscreen="true"
            max-width="500">
              <template v-slot:default="dialog">
                  <v-card>
                    <v-toolbar
                    color="primary"
                    dark>
                      Enter your name please
                    </v-toolbar>

                    <NameForm @nameGot="getTodos" v-bind:status="getStatus"/>

                    <v-card-actions class="justify-end">
                      <v-btn id="enterNameCloseButton"
                      text
                      color="red"
                      @click="dialog.value = false">
                      </v-btn>
                    </v-card-actions>
                  </v-card>
              </template>
            </v-dialog>
        </v-col>
  </v-row>
</template>

<script>
import NameForm from './verifyForm/Form.vue'
import axios from 'axios'
import serverConfig from '@/user-config.js'

export default {
  name: 'VerifyUser',
  components: {
    NameForm
  },
  methods: {
    closeDialog () {
      document.getElementById('enterNameCloseButton').click()
    },
    handleGetResponse (response) {
      console.log(response)
      this.loading = true
      this.closeDialog()
      response.data.forEach(element => this.todoArray.push(element))
      this.$store.commit('setTodos', this.todoArray)
    },
    handleGetError (error) {
      console.log(error)
      if (error.response.status === 401) {
        this.$store.commit('setNotificationMessage', 'Create new Todo from the top right')
        this.$emit('notify', '')
        this.closeDialog()
      } else {
        this.loading = false
      }
    },
    getTodos (name) {
      const request = serverConfig.api.baseUrl + serverConfig.routerOptions.main + serverConfig.routerOptions.get + name
      axios.get(request)
        .then(this.handleGetResponse)
        .catch(this.handleGetError)
    }
  },
  computed: {
    getStatus () {
      return this.loading
    }
  },
  data () {
    return {
      loading: true,
      todoArray: []
    }
  }
}
</script>
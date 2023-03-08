import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    username: '',
    order: '',
    todos: [],
    notification: ''
  },
  getters: {
  },
  mutations: {
    setUsername (state, name) {
      state.username = String(name)
    },
    updateOrder (state, order) {
      state.order = String(order)
    },
    setTodos (state, json) {
      state.todos = [...json]
    },
    addTodo (state, json) {
      state.todos.push(json)
    },
    setNotificationMessage (state, message) {
      state.notification = String(message)
    }
  },
  actions: {
  },
  modules: {
  }
})
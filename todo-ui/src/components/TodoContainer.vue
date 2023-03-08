<template>
    <v-container class="todoContainer my-2"
    dense
    outlined
    rounded>
        <v-row v-if="todos.length > 0" >
            <v-col cols="12" class="text-center"
            v-bind:key="todo.title" v-for="todo in todos">
                <ToDo :to-do="todo"/>
            </v-col>
        </v-row>
        <v-row v-else >
            <v-col cols="12" class="text-center grey--text text--lighten-1">
                <h1>PERFEKT</h1>
                <h1>You haven't any Todos</h1>
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
import ToDo from './todoCard/Todo.vue'

export default {
  name: 'TodoContainer',
  components: {
    ToDo
  },
  props: ['todos']
}
</script>

<style>
    .todoContainer{
        background-color: #37474F;
        width: 80%;
         margin: auto;
    }
</style>
<template>
    <v-card
    color="blue-grey darken-2"
    elevation="5"
    outlined
    shaped>
        <v-card-title v-text="toDo.title" class="grey--text text--lighten-2"></v-card-title>
        <v-card-text v-text="toDo.note" class="black--text"></v-card-text>

        <v-container>
            <v-row>
                <v-col cols="4" class="text-left light-green--text text--darken-2">
                    <p v-text="toDo.creationdate"></p>
                </v-col>

                <v-col cols="4" class="text-center">
                    <v-icon color="#ffad33">mdi-arrow-right</v-icon>
                </v-col>

                <v-col cols="4" class="text-right red--text text--accent-4">
                    <p v-text="toDo.duedate"></p>
                </v-col>
            </v-row>
        </v-container>
    </v-card>
</template>

<script>
export default {
  name: 'ToDo',
  props: ['toDo']
}
</script>

<style>
</style>
<template>
    <v-form
    ref="form"
    v-model="valid"
    lazy-validation>
    <v-container>
      <v-row justify="">
          <v-col>
            <v-text-field
            v-model="newTodo.title"
            :counter="100"
            :rules="[v => !!v || 'title is required', v => (v && v.length <= 100) || 'title must be less than 100 characters']"
            label="title"
            filled
            required
            ></v-text-field>
          </v-col>
          <v-col>
            <v-textarea
            solo
            name="input-7-4"
            v-model="newTodo.note"
            :rules="[v => !!v || 'note is required', v => (v && v.length <= 500) || 'note must be less than 500 characters']"
            :counter="500"
            label="note"
            required
            ></v-textarea>
          </v-col>
      </v-row>

      <v-row align="center" justify="center">
          <v-col cols="6">
              <v-date-picker
                v-model="picker"
                year-icon="mdi-calendar-blank"
                required
                ></v-date-picker>
          </v-col>
      </v-row>
    </v-container>

    <div v-if="status === false" class="text-center mb-10">
      <v-progress-circular
      :size="20"
      color="grey darken-1"
      indeterminate
      ></v-progress-circular>
    </div>

    <v-btn
      :disabled="!valid"
      color="success"
      class="mr-4 createButton"
      @click="validate">
        Create
    </v-btn>

    <v-btn
      color="error"
      class="mr-4"
      @click="reset"
    >
      Reset Form
    </v-btn>

    <v-btn id="taskFormResetValidationButton"
      color="warning"
      @click="resetValidation"
      class="mr-4"
    >
      Reset Validation
    </v-btn>
  </v-form>
</template>

<script>
export default {
  name: 'TaskForm',
  props: ['status'],
  methods: {
    validate () {
      if (this.$refs.form.validate()) {
        const today = new Date()
        this.newTodo.creationDate = today.toJSON().slice(0, 10).replace(/-/g, '.')
        this.newTodo.dueDate = String(this.picker).replace(/-/g, '.')
        this.$emit('todoCreated', this.newTodo)
        this.newTodo.title = ''
        this.newTodo.note = ''
        this.newTodo.creationDate = ''
        this.newTodo.dueDate = ''
        this.picker = null

        document.getElementById('taskFormResetValidationButton').click()
      }
    },
    reset () {
      this.$refs.form.reset()
    },
    resetValidation () {
      this.$refs.form.resetValidation()
    }
  },
  data () {
    return {
      valid: true,
      picker: null,
      newTodo: {
        title: '',
        note: '',
        creationDate: '',
        dueDate: ''
      }
    }
  }
}
</script>

<style>
  .createButton {
    margin-left: 3%;
  }
</style>
<template>
    <v-form
    ref="form"
    v-model="valid"
    lazy-validation>
        <v-container>
            <v-row>
                <v-col>
                    <v-text-field
                    v-model="username"
                    :counter="100"
                    :rules="[v => !!v || 'title is required', v => (v && v.length <= 100) || 'title must be less than 100 characters']"
                    label="your Name"
                    filled
                    required
                    ></v-text-field>
                </v-col>
            </v-row>
        </v-container>

        <v-btn
        :disabled="!valid"
        color="success"
        class="mr-4 submitButton"
        @click="validate">
            submit
        </v-btn>

        <v-btn
        color="error"
        class="mr-4"
        @click="reset">
            Reset Form
        </v-btn>

        <v-btn id="formResetValidationButton"
        color="warning"
        @click="resetValidation">
            Reset Validation
        </v-btn>

        <div v-if="status === false" class="text-center mt-10">
          <v-progress-circular
            :size="100"
            color="grey darken-1"
            indeterminate
          ></v-progress-circular>
        </div>
    </v-form>
</template>

<script>
export default {
  name: 'NameForm',
  props: ['status'],
  methods: {
    validate () {
      if (this.$refs.form.validate()) {
        document.getElementById('formResetValidationButton').click()
        this.$store.commit('setUsername', this.username)
        this.$emit('nameGot', this.username)
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
      username: '',
      valid: true
    }
  }
}
</script>

<style>
  .submitButton {
    margin-left: 4%;
  }
</style>
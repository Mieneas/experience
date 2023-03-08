<template>
    <v-toolbar class="toolbar"
    color="#37474F"
    dense
    elevation="10"
    outlined
    rounded>
        <v-container>
          <v-row>
            <v-col cols="4" class="text-center">
              <v-btn
                color="orange"
                text
                v-on:click="sendOrder('')"
              >as created</v-btn>
            </v-col>

            <v-col cols="4" class="text-center">
              <v-btn
                color="orange"
                text
                v-on:click="sendOrder('n')"
              >ascending</v-btn>
            </v-col>

            <v-col cols="4" class="text-center">
              <v-btn
                color="orange"
                text
                v-on:click="sendOrder('f')"
              >descending</v-btn>
            </v-col>
          </v-row>
        </v-container>
    </v-toolbar>
</template>

<script>
export default {
  name: 'ToolBar',
  methods: {
    sendOrder (order) {
      this.$store.commit('updateOrder', order)
    }
  }
}
</script>

<style>
  .toolbar{
    width: 80%;
    margin: auto;
  }
</style>
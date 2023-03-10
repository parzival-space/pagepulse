<template>
  <h1>Hello World</h1>
  <ServiceGroup></ServiceGroup>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import ServiceGroup from './components/ServiceGroup.vue';

export default defineComponent({
  name: 'App',
  components: {
    ServiceGroup
  }
});
</script>

<style lang="scss">
html {
  color: white;
  background: #0b0f1a;
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  margin-top: 60px;
}
</style>

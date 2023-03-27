<template>
  <h1>Service Status</h1>
  <ServiceList></ServiceList>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import ServiceList from './components/ServiceList.vue';

export default defineComponent({
  name: 'App',
  components: {
    ServiceList
  }
});
</script>

<style lang="scss">
html, body {
  min-width: 100%;
  min-height: 100%;
  height: 100%;
  margin: 0;
  color: white;
  background: #0b0f1a;
}

#app {
  display: flex;
  padding: 4rem 10%;
  box-sizing: border-box;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100%;

  font-family: "Poppins", sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;

  h1 {
    text-align: center;
    font-size: 3rem;
    line-height: 1.5;
    margin: 0;
    padding: 0;
    border: 0;
  }
}
</style>

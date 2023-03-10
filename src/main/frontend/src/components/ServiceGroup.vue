<template>
  <div class="wrapper">
    <Service serviceTitle="Website"></Service>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import Service from './Service.vue';

export default defineComponent({
  name: 'ServiceGroup',
  components: {
    Service
  }
});
</script>

<style lang="scss" scoped>
.wrapper {
  border: 1px solid grey;
  width: 40rem;
  max-width: 100%;
  margin: 4rem auto 2rem auto;
  border-radius: 5px;
}
</style>

<template>
  <div class="serviceGroup">
    <h1 class="group">{{  data[0].group }}</h1>
    <Service class="service" v-for="service in data" :data="service"></Service>
  </div>
</template>

<script lang="ts">
import { defineComponent, PropType } from 'vue';
import { IService } from '../types/IService';
import Service from './Service.vue';

export default defineComponent({
  name: 'ServiceGroup',
  components: {
    Service
  },

  props: {
    "data": { type: Array as PropType<IService[]>, default: [] }
  }
});
</script>

<style lang="scss" scoped>
$border: 1px solid grey;

.serviceGroup {
  border: $border;
  width: 40rem;
  max-width: 100%;
  margin: 0rem auto 0rem auto;
  border-radius: 5px;

  +:not(:first-child) {
    margin-top: 1.5rem !important;
  }

  .group {
    font-size: 1.4rem !important;
    margin-top: .3rem !important;
    margin-bottom: .3rem !important;
  }

  .service:not(:first-child) {
    border-top: $border;
  }
}
</style>

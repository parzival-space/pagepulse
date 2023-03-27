<template>
  <div class="serviceList">
    <ServiceGroup class="group" v-for="group in groups" v-bind:data=group></ServiceGroup>
  </div>
</template>

<script lang="ts">
import ServiceGroup from './ServiceGroup.vue';
import axios, { AxiosResponse } from 'axios';
import { IService } from '../types/IService'
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'ServiceList',
  components: {
    ServiceGroup
  },

  data() {
    return {
      "groups": [] as IService[][]
    }
  },

  async created() {
    let response: AxiosResponse = await axios.get('/api/services');
    let services: IService[] = response.data;

    let groups: { [key: string]: IService[] } = {};
    services.forEach(service => {
      let group = service.group;
      if (group in groups) {
        groups[group].push(service);
      } else {
        groups[group] = [service];
      }
    })
    
    this.groups = Object.values(groups);
  }
});
</script>

<style lang="scss" scoped>
.serviceList {
  margin: 4rem auto 2rem auto;
}
</style>

<template>
  <a href="javascript:void(0)">
    {{ serviceTitle }}
  </a>
</template>

<script lang="ts">
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'Service',
  components: {},
  props: {
    "serviceTitle": String
  }
});
</script>

<style lang="scss" scoped>
template a {
  color: red;
}
</style>

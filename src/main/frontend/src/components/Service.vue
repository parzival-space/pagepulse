<template>
  <div class="service" @click="showDetails = !showDetails">
    <div class="header">
      <p class="title">{{ data.name }} <a :href="data.endpoint">({{ data.endpoint }})</a></p>
      <p class="status" :status="checkResult.status">{{ checkResult.status[0].toLocaleUpperCase() + checkResult.status.substring(1).toLocaleLowerCase() }}</p> <!-- TODO: implement this -->
    </div>
    <div class="details" v-if="showDetails">
      <p>Last Check:</p>
      <p>{{ checkResult.timestamp.toLocaleDateString() }} at {{ checkResult.timestamp.toLocaleTimeString() }}</p>
      <p>Error Message:</p>
      <p>{{ checkResult.error }}</p>
      <p>Possible Cause:</p>
      <p>{{ checkResult.possibleCause }}</p>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, PropType } from 'vue';
import { IHistoryEntry } from '../types/IHistoryEntry';
import { IService } from '../types/IService';
import { Status } from '../types/Status';
import axios, { AxiosResponse } from 'axios';

export default defineComponent({
  name: 'Service',
  components: {},
  props: {
    "data": { type: Object as PropType<IService>, default: {} }
  },
  data() {
    return {
      "checkResult": { status: Status.UNKNOWN, error: "", timestamp: new Date(), possibleCause: "" },
      "showDetails": false
    }
  },

  async created() {
    let response: AxiosResponse = await axios.get(`/api/history?serviceId=${this.data.id}`);
    let history: IHistoryEntry[] = response.data;

    console.log(history)

    let error = history[0].error as string;
    let possibleCause = history[0].possibleCause as string;

    this.checkResult = {
      status: history[0].status,
      timestamp: new Date(history[0].timestamp as string),
      error: error == null ? "Working as expected." : error ,
      possibleCause: possibleCause == null ? "Working as expected." : possibleCause 
    }
  }
});
</script>

<style lang="scss" scoped>
.service {
  .header {
    display: grid;
    grid-template-columns: max-content max-content;
    gap: 1rem;
    place-content: space-between;
    padding: 1rem;

    p {
      margin: 0;
      padding: 0;
      border: 0;

      a {
        color: gray;

        +:hover {
          color: aqua;
        }
      }

      +[status="OPERATIONAL"] {
        color: lime;
      }

      +[status="LIMITED"] {
        color: yellow;
      }

      +[status="OFFLINE"] {
        color: red;
      }

      +[status="UNKNOWN"] {
        color: gray;
      }
    }
  }

  .details {
    display: grid;
    grid-template-columns: max-content 50%;
    place-content: space-between;
    overflow: auto;
    word-wrap: normal;
    max-width: 100%;

    background-color: #0e1422;
    padding: 1rem;
  }
}
</style>

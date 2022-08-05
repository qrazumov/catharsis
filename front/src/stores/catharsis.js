import {defineStore} from 'pinia';

export const useCatharsisStore = defineStore('data', {
  state: () => ({
    data: {}
  }),

  getters: {
    getData: (state) => state.data,

  },

  actions: {
    setData(data) {
      this.data = data
    }
  }
})

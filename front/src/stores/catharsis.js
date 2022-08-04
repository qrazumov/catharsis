import {defineStore} from 'pinia';

export const usePostStore = defineStore('post', {
  state: () => ({
    post: {}
  }),

  getters: {
    getPost: (state) => state.post,

  },

  actions: {
    setPost(data) {
      this.post = data
    }
  }
})

<template>
  <PostsTmpl ref="postsTmpl" :items="items" :on-load="onLoad"></PostsTmpl>
</template>

<script>
import {defineComponent, ref} from 'vue'
import {useQuasar} from 'quasar'
import PostService from '../service/post.service'
import PostsTmpl from 'components/PostsTmpl'
import {cutLongString} from '../util/helper'
import {useCatharsisStore} from "stores/catharsis"

export default defineComponent({
  name: 'PostsPage',
  components: {
    PostsTmpl
  },
  async preFetch({store}) {
    const myStore = useCatharsisStore(store)
    const maxTextLength = 300
    let res = await PostService.getPosts(0, 4)
    for (let i = 0; i < res.data.length; i++)
      res.data[i].text = cutLongString(res.data[i].text.replace(/<(.|\n)*?>/g, ''), maxTextLength)
    myStore.setData(res.data)
  },
  setup() {

    const items = ref([])
    const offset = ref(4)
    const postsTmpl = ref(null)
    const $q = useQuasar()
    const notify = (useQuasar) => {
      useQuasar.notify({
        progress: true,
        color: 'negative',
        position: 'top',
        message: 'Ошибка загрузки',
        icon: 'report_problem'
      })
    }

    const onLoad = (index, done) => {

      if (items.value !== [])
        setTimeout(() => {
          PostService.getPosts(offset.value, 4)
            .then((response) => {
              if (response.data.length === 0) postsTmpl.value.infPost.stop()
              items.value = items.value.concat(response.data)
            })
            .catch(() => {
              notify($q)
            })
          offset.value += 4
          done()

        }, 1000)
    }

    const postsStore = useCatharsisStore()
    items.value = postsStore.getData

    return {
      items,
      postsTmpl,
      onLoad
    };
  }
})
</script>


<template>
  <PostsTmpl ref="postsTmpl" :items="items" :on-load="onLoad"></PostsTmpl>
</template>

<script>
import {defineComponent, onMounted, ref} from 'vue'
import {useQuasar} from 'quasar'
import PostService from '../service/post.service'
import PostsTmpl from 'components/PostsTmpl'
import {cutLongString} from '../util/helper'


export default defineComponent({
  name: 'PostsPage',
  components: {
    PostsTmpl
  },
  setup() {

    const items = ref([])
    const offset = ref(4)
    const postsTmpl = ref(null)
    const $q = useQuasar()
    const notify = (useQuasar) => {
      useQuasar.notify({
        color: 'negative',
        position: 'top',
        message: 'Loading failed',
        icon: 'report_problem'
      })
    }
    const maxTextLength = 300

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

    onMounted(() => {

      PostService.getPosts(0, 4)
        .then((response) => {
          items.value = response.data
          for (let i = 0; i < items.value.length; i++)
            items.value[i].text = cutLongString(items.value[i].text.replace(/<(.|\n)*?>/g, ''), maxTextLength)
        })
        .catch(() => {
          notify($q)
        })
    })

    return {
      items,
      postsTmpl,
      onLoad
    };
  }
})
</script>


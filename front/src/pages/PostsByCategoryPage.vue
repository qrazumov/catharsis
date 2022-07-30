<template>
  <PostsTmpl ref="postsTmpl" :items="items" :on-load="onLoad"></PostsTmpl>
</template>

<script>
import {defineComponent, onMounted, ref} from 'vue'
import {useQuasar} from 'quasar'
import PostService from '../service/post.service'
import PostsTmpl from 'components/PostsTmpl'
import {useRoute} from 'vue-router'

export default defineComponent({
  name: 'PostsByCategoryPage',
  components: {
    PostsTmpl
  },
  setup() {

    const items = ref([])
    const offset = ref(4)
    const postsTmpl = ref(null)
    const route = useRoute()
    const $q = useQuasar()
    const notify = (useQuasar) => {
      useQuasar.notify({
        color: 'negative',
        position: 'top',
        message: 'Loading failed',
        icon: 'report_problem'
      })
    }
    const onLoad = (index, done) => {
      if (items.value !== [])
        setTimeout(() => {
          PostService.getPostsByCategory(route.params.id, offset.value, 4)
            .then((response) => {
              if (response.data.length === 0) postsTmpl.value.infPost.stop()
              items.value = items.value.concat(response.data)
            })
            .catch(() => {
              notify($q)
            })
          offset.value += 4
          done()

        }, 2000)
    }

    onMounted(() => {

      PostService.getPostsByCategory(route.params.id, 0, 4)
        .then((response) => {
          items.value = response.data
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


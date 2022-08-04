<template>
  <PostsTmpl ref="postsTmpl" :items="items" :on-load="onLoad"></PostsTmpl>
</template>

<script>
import {defineComponent, onMounted, ref} from 'vue'
import {useMeta, useQuasar} from 'quasar'
import PostService from '../service/post.service'
import PostsTmpl from 'components/PostsTmpl'
import {useRoute} from 'vue-router'
import {cutLongString} from "src/util/helper";

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
    const maxTextLength = 300


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
          for (let i = 0; i < items.value.length; i++)
            items.value[i].text = cutLongString(items.value[i].text, maxTextLength)
          useMeta(() => {
            return {
              title: items.value[0].category.name,
              titleTemplate: title => `${title} - razymov.tech`,
              meta: {
                description: {name: 'description', content: items.value[0].category.name},
                keywords: {name: 'keywords', content: items.value[0].category.name},
                equiv: {'http-equiv': 'Content-Type', content: 'text/html; charset=UTF-8'},
              },
            }
          })
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


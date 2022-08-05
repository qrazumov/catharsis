<template>
  <PostsTmpl
    v-if="items.length !== 0"
    ref="postsTmpl" :items="items" :on-load="onLoad"></PostsTmpl>
  <q-page v-else>
    <div class="q-pa-md q-gutter-sm row justify-center">
      <q-card class="my-card" flat square>
        <h4>category is empty</h4>
      </q-card>
    </div>
  </q-page>
</template>

<script>
import {defineComponent, ref} from 'vue'
import {useMeta, useQuasar} from 'quasar'
import PostService from '../service/post.service'
import PostsTmpl from 'components/PostsTmpl'
import {useRoute} from 'vue-router'
import {cutLongString} from "src/util/helper";
import {useCatharsisStore} from "stores/catharsis"

export default defineComponent({
  name: 'PostsByCategoryPage',
  components: {
    PostsTmpl
  },
  async preFetch({store, currentRoute}) {
    const myStore = useCatharsisStore(store)
    const maxTextLength = 300
    let res = await PostService.getPostsByCategory(currentRoute.params.id, 0, 4)
    for (let i = 0; i < res.data.length; i++)
      res.data[i].text = cutLongString(res.data[i].text, maxTextLength)
    myStore.setData(res.data)
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

    const postsStore = useCatharsisStore()
    items.value = postsStore.getData

    if(items.value.length !== 0)
      useMeta({
        title: items.value[0].category.name,
        titleTemplate: title => `${title} - razymov.tech`,
        meta: {
          description: {name: 'description', content: items.value[0].category.name},
          keywords: {name: 'keywords', content: items.value[0].category.name},
          equiv: {'http-equiv': 'Content-Type', content: 'text/html; charset=UTF-8'},
        },
      })

    return {
      items,
      postsTmpl,
      onLoad
    };
  }
})
</script>


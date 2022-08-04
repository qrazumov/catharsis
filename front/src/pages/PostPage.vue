<template>
  <q-page class="flex-start">
    <div class="q-pa-md row items-start q-gutter-md">
      <div
        v-if="item != {}"
        class="my-card">
        <q-card-section>
          <h1 class="text-h6">{{ item.name }}</h1>
          <q-badge color="blue">
            {{ category }}
          </q-badge>
        </q-card-section>
        <q-card-section class="q-pt-none">
          <div v-html="item.text"></div>
        </q-card-section>
        <q-btn class="q-pa-md" flat icon="event" round/>
        <q-btn class="q-pa-md" flat>
          <div v-html="item.createdAt"></div>
        </q-btn>
      </div>
    </div>
  </q-page>
</template>

<script>
import {defineComponent, ref} from 'vue'
import {useMeta} from 'quasar'
import PostService from '../service/post.service'
import {usePostStore} from "stores/catharsis"

export default defineComponent({
  name: 'PostPage',
  async preFetch({store, currentRoute}) {
    const myStore = usePostStore(store)
    const id = currentRoute.params.id
    let res = await PostService.getPost(id);
    myStore.setPost(res.data)
  },

  setup() {

    const myStore = usePostStore()
    const item = ref({})
    const category = ref({})
    const title = ref(null)

    item.value = myStore.getPost
    category.value = item.value.category.name
    title.value = item.value.name

    useMeta({
      title: title.value,
      titleTemplate: title => `${title} - razymov.tech`,
      meta: {
        description: {name: 'description', content: title.value},
        keywords: {name: 'keywords', content: title.value},
        equiv: {'http-equiv': 'Content-Type', content: 'text/html; charset=UTF-8'},
      },
    })

    return {
      item,
      category
    }
  }

})
</script>


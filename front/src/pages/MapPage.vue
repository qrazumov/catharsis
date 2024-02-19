<template>
  <q-page>
    <div class="q-pa-md">
      <p>Карта</p>
      <q-list bordered separator>
        <div v-for="item in items" :key="item" v-ripple
             class="q-pa-md q-gutter-sm"
             clickable
        >
          <p>
            Категория: {{ item.name }}
          </p>
          <a
            v-for="post in item.posts"
            :key="post"
            :href="/post/ + post.id"
            class="row text-bold text-blue-grey-10"
            size="sm"
          >
            [{{ post.id }}] {{ post.name }}
          </a>
        </div>
      </q-list>
    </div>
  </q-page>
</template>

<script>
import {defineComponent, ref} from 'vue'
import {useMeta} from 'quasar'
import PostService from '../service/post.service'
import {useCatharsisStore} from "stores/catharsis"

export default defineComponent({
  name: 'MapPage',
  async preFetch({store}) {
    const myStore = useCatharsisStore(store)
    let res = await PostService.getCategories()
    myStore.setData(res.data)
  },
  setup() {

    const items = ref(null)
    const myStore = useCatharsisStore()
    items.value = myStore.getData
    useMeta({
      title: "map",
      titleTemplate: title => `${title} - razumov.tech`,
      meta: {
        description: {name: 'description', content: "map"},
        keywords: {name: 'keywords', content: "map"},
        equiv: {'http-equiv': 'Content-Type', content: 'text/html; charset=UTF-8'},
      },
    })


    return {
      items
    };
  }
})
</script>


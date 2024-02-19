<template>
  <q-page>
    <div class="q-pa-md">
      <p>Категории</p>
      <q-list bordered separator>
        <q-item v-for="item in items" :key="item"
                v-ripple
                clickable
        >
          <q-item-section>
            <div color="primary" @click="$router.push('/category/' + item.id)">{{ item.name }}</div>
          </q-item-section>
        </q-item>
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
  name: 'CategoryPage',
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
      title: "category",
      titleTemplate: title => `${title} - razumov.tech`,
      meta: {
        description: {name: 'description', content: "category"},
        keywords: {name: 'keywords', content: "category"},
        equiv: {'http-equiv': 'Content-Type', content: 'text/html; charset=UTF-8'},
      },
    })
    return {
      items
    }

  }

})
</script>


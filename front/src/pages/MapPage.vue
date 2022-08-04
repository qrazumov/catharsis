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
          <q-btn
            v-for="post in item.posts"
            :key="post"
            class="row"
            color="primary"
            size="sm" @click="$router.push('/post/' + post.id)">[{{ post.id }}] {{ post.name }}
          </q-btn>
        </div>
      </q-list>
    </div>
  </q-page>
</template>

<script>
import {defineComponent, onMounted, ref} from 'vue'
import {useMeta, useQuasar} from 'quasar'
import PostService from '../service/post.service'

export default defineComponent({
  name: 'MapPage',
  setup() {

    const items = ref(null)
    const $q = useQuasar()
    useMeta({
      title: "map",
      titleTemplate: title => `${title} - razymov.tech`,
      meta: {
        description: {name: 'description', content: "map"},
        keywords: {name: 'keywords', content: "map"},
        equiv: {'http-equiv': 'Content-Type', content: 'text/html; charset=UTF-8'},
      },
    })

    onMounted(() => {

      const $q = useQuasar()

      PostService.getCategories()
        .then((response) => {
          items.value = response.data
        })
        .catch(() => {
          $q.notify({
            color: 'negative',
            position: 'top',
            message: 'Loading failed',
            icon: 'report_problem'
          })
        })

    })

    return {
      items
    };
  }
})
</script>


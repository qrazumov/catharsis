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
import {defineComponent, onMounted, ref} from 'vue'
import {useMeta, useQuasar} from 'quasar'
import PostService from '../service/post.service'

export default defineComponent({
  name: 'CategoryPage',
  setup() {

    const items = ref(null)
    useMeta({
      title: "category",
      titleTemplate: title => `${title} - razymov.tech`,
      meta: {
        description: { name: 'description', content: "category" },
        keywords: { name: 'keywords', content: "category" },
        equiv: { 'http-equiv': 'Content-Type', content: 'text/html; charset=UTF-8' },
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
    }

  }

})
</script>


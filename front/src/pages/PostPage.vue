<template>
  <q-page class="flex-start">
    <div class="q-pa-md row items-start q-gutter-md">
      <div
        v-if="item != {}"
        class="my-card">
        <q-card-section>
          <div class="text-h6">{{ item.name }}</div>
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
import {defineComponent, onMounted, ref} from 'vue'
import {useQuasar} from 'quasar'
import PostService from '../service/post.service'
import {useRoute} from 'vue-router'
import {useMeta} from "quasar"

export default defineComponent({
  name: 'PostPage',
  setup() {

    const route = useRoute();
    const $q = useQuasar()
    const id = ref(route.params.id)
    const item = ref({
      createdAt: null
    })
    const category = ref({})
    const title = ref(null)

    onMounted(() => {

      PostService.getPost(id.value)
        .then((response) => {
          item.value = response.data
          category.value = response.data.category.name
          title.value = item.value.name

          useMeta(() => {
            return {
              title: title.value,
              titleTemplate: title => `${title} - razymov.tech`,
              meta: {
                description: { name: 'description', content: title.value },
                keywords: { name: 'keywords', content: title.value },
                equiv: { 'http-equiv': 'Content-Type', content: 'text/html; charset=UTF-8' },
              },
            }
          })
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
      item,
      category
    }
  }

})
</script>


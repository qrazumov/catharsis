<template>
  <q-page class="flex-start">
    <div class="q-pa-md row items-start q-gutter-md">
      <q-card
        v-if="item != {}"
        class="my-card col">
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
          {{ item.createdAt }}
        </q-btn>
      </q-card>
    </div>
  </q-page>
</template>

<script>
import {defineComponent, onMounted, ref} from 'vue'
import {useQuasar} from 'quasar'
import PostService from '../service/post.service'
import {useRoute} from 'vue-router';

export default defineComponent({
  name: 'PostPage',
  setup() {

    const route = useRoute();
    const $q = useQuasar()
    const id = ref(route.params.id)
    const item = ref({})
    const category = ref(null)

    onMounted(() => {

      PostService.getPost(id.value)
        .then((response) => {
          item.value = response.data
          category.value = response.data.category.name
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


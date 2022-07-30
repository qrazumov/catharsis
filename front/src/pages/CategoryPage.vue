<template>
  <q-page>
    <div class="q-pa-md" style="max-width: 350px">
      <p>Категории</p>
      <q-list bordered separator>
        <q-item v-for="item in items" :key="item"
                v-ripple
                clickable
        >
          <q-item-section>
            <q-btn color="primary" @click="$router.push('/category/' + item.id)">{{ item.name }}</q-btn>
          </q-item-section>
        </q-item>
      </q-list>
    </div>
  </q-page>
</template>

<script>
import {defineComponent, onMounted, ref} from 'vue'
import {useQuasar} from 'quasar'
import PostService from '../service/post.service'

export default defineComponent({
  name: 'CategoryPage',
  setup() {

    const items = ref(null)

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
      items: []
    }

  }

})
</script>


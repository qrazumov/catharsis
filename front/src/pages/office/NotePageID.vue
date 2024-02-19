<template>
  <NotesTmpl
    :categories="categories"
    :category="category"
    :cnt="content"
    :name="name"
    :on-submit="onSubmit"
    :preview="preview"
    :video="video"
    :visible="visible"
    @change-name="handleChangeName"
    @change-category="handleChangeCategory"
    @change-preview="handleChangePreview"
    @change-video="handleChangeVideo"
    @change-content="handleChangeContent"
  />
</template>
<script>
import {defineComponent, onMounted, ref} from 'vue'
import {Cookies, useQuasar} from 'quasar'
import PostService from "src/service/post.service"
import NotesTmpl from "components/office/NotesTmpl"
import lodash from 'lodash'
import {useRouter, useRoute} from "vue-router"


export default defineComponent({
  name: 'NotePage',
  components: {
    NotesTmpl
  },
  setup() {

    const $router = useRouter()

    onMounted(() => {
      console.log(Cookies.get('user'))
      if (!Cookies.has('user')) {
        $router.push('/office/login');
      }
    })

    const handleChangeName = (s) => {
      name.value = s
    }
    const handleChangeCategory = (s) => {
      category.value = lodash.groupBy(categories.value, 'id')[s][0]
    }
    const handleChangePreview = (s) => {
      preview.value = s
    }
    const handleChangeVideo = (s) => {
      video.value = s
    }
    const handleChangeContent = (s) => {
      content.value = s
    }

    const name = ref(null)
    const category = ref(null)
    const categories = ref(null)
    const preview = ref(null)
    const video = ref(null)
    const $q = useQuasar()
    const content = ref(null)
    const item = ref(null)
    const visible = ref(false)
    const route = useRoute()

    const notify = (useQuasar) => {
      useQuasar.notify({
        progress: true,
        color: 'negative',
        position: 'top',
        message: 'Ошибка загрузки',
        icon: 'report_problem'
      })
    }

    const onSubmit = () => {

      PostService.patchPost(route.params.id, {
        name: name.value,
        text: content.value,
        category: {id: category.value.id},
        img: preview.value,
        video: video.value
      })
        .then((response) => {
          $q.notify({
            color: 'secondary',
            position: 'top',
            message: 'Заметка опубликована',
            icon: 'done'
          })
        })
        .catch(() => {
          notify($q)
        })

    }

    onMounted(() => {

      visible.value = true

      PostService.getPost(route.params.id)
        .then((response) => {
          item.value = response.data
          name.value = item.value.name
          category.value = {id: item.value.category.id, name: item.value.category.name}
          content.value = item.value.text
          preview.value = item.value.img
          video.value = item.value.video
        })
        .catch(() => {
          notify($q)
        })

      PostService.getCategories()
        .then((response) => {
          categories.value = response.data
        })
        .catch(() => {
          notify($q)
        })
    })

    return {
      name,
      category,
      categories,
      preview,
      onSubmit,
      content,
      visible,
      video,
      handleChangeName,
      handleChangeCategory,
      handleChangeVideo,
      handleChangePreview,
      handleChangeContent,
      item
    }
  }
})
</script>


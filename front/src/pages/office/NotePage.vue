<template>
  <NotesTmpl
    @change-name="handleChangeName"
    @change-category="handleChangeCategory"
    @change-preview="handleChangePreview"
    @change-video="handleChangeVideo"
    @change-content="handleChangeContent"
    :name="name"
    :on-submit="onSubmit"
    :category="category"
    :categories="categories"
    :preview="preview"
    :video="video"
    :visible="visible"

  />
</template>
<script>
import {defineComponent, onMounted, ref} from 'vue'
import {useQuasar} from 'quasar'
import PostService from "src/service/post.service"
import NotesTmpl from "components/office/NotesTmpl"
import lodash from 'lodash'


export default defineComponent({
  name: 'NotePage',
  components: {
    NotesTmpl
  },
  setup() {

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
      console.log(s)
      content.value = s
    }

    const name = ref(null)
    const category = ref(null)
    const categories = ref(null)
    const preview = ref(null)
    const video = ref(null)
    const $q = useQuasar()
    const content = ref(null)
    const visible = ref(false)

    const notify = (useQuasar) => {
      useQuasar.notify({
        color: 'negative',
        position: 'top',
        message: 'Loading failed',
        icon: 'report_problem'
      })
    }

    const onSubmit = () => {

      PostService.savePost({
        name: name.value,
        text: content.value,
        category: category.value,
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
      handleChangeContent
    }
  }
})
</script>


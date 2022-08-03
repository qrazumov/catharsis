<template>
  <q-page class="flex-start q-pa-md">
    <div class="q-pa-md q-gutter-sm">
      <q-form
        class="q-gutter-md"
        @reset="onReset"
        @submit="onSubmit"
      >
        <p>Новая запись</p>
        <q-input v-model="name" label="Название"/>
        <q-select v-model="category" :options="categories" label="Категория"/>
        <q-file v-model="preview" bottom-slots counter filled label="превью">
          <template v-slot:prepend>
            <q-icon name="image" @click.stop.prevent/>
          </template>
          <template v-slot:append>
            <q-icon class="cursor-pointer" name="close" @click.stop.prevent="preview = null"/>
          </template>
          <template v-slot:hint>
            изображение
          </template>
        </q-file>
        <q-editor
          v-model="qeditor"
          :dense="$q.screen.lt.md"
          :fonts="{
        arial: 'Arial',
        arial_black: 'Arial Black',
        comic_sans: 'Comic Sans MS',
        courier_new: 'Courier New',
        impact: 'Impact',
        lucida_grande: 'Lucida Grande',
        times_new_roman: 'Times New Roman',
        verdana: 'Verdana'
      }"
          :toolbar="[
        [
          {
            label: $q.lang.editor.align,
            icon: $q.iconSet.editor.align,
            fixedLabel: true,
            list: 'only-icons',
            options: ['left', 'center', 'right', 'justify']
          },
          {
            label: $q.lang.editor.align,
            icon: $q.iconSet.editor.align,
            fixedLabel: true,
            options: ['left', 'center', 'right', 'justify']
          }
        ],
        ['bold', 'italic', 'strike', 'underline', 'subscript', 'superscript'],
        ['token', 'hr', 'link', 'custom_btn'],
        ['print', 'fullscreen'],
        [
          {
            label: $q.lang.editor.formatting,
            icon: $q.iconSet.editor.formatting,
            list: 'no-icons',
            options: [
              'p',
              'h1',
              'h2',
              'h3',
              'h4',
              'h5',
              'h6',
              'code'
            ]
          },
          {
            label: $q.lang.editor.fontSize,
            icon: $q.iconSet.editor.fontSize,
            fixedLabel: true,
            fixedIcon: true,
            list: 'no-icons',
            options: [
              'size-1',
              'size-2',
              'size-3',
              'size-4',
              'size-5',
              'size-6',
              'size-7'
            ]
          },
          {
            label: $q.lang.editor.defaultFont,
            icon: $q.iconSet.editor.font,
            fixedIcon: true,
            list: 'no-icons',
            options: [
              'default_font',
              'arial',
              'arial_black',
              'comic_sans',
              'courier_new',
              'impact',
              'lucida_grande',
              'times_new_roman',
              'verdana'
            ]
          },
          'removeFormat'
        ],
        ['quote', 'unordered', 'ordered', 'outdent', 'indent'],

        ['undo', 'redo'],
        ['viewsource']
      ]"
        />
        <editor
          api-key="ybri4nrrhkarhhvpii1q7y66tjjqgvd3ckhjyjx7rv1g2u9s"
          ref="tiny"
          v-model="content"
          :init="{
         height: 500,
         menubar: false,
         plugins: [
           'print preview paste importcss searchreplace autolink autosave save directionality code visualblocks visualchars fullscreen image link media template codesample table charmap hr pagebreak nonbreaking anchor toc insertdatetime advlist lists wordcount imagetools textpattern noneditable help charmap quickbars emoticons'
         ],
         toolbar:
           'undo redo | bold italic underline strikethrough | fontselect fontsizeselect formatselect | alignleft aligncenter alignright alignjustify | outdent indent |  numlist bullist | forecolor backcolor removeformat | pagebreak | charmap emoticons | fullscreen  preview save print | insertfile image media template link anchor codesample | ltr rtl',
       menubar: 'file edit view insert format tools table help',
          }"
        />
        <q-btn color="primary" icon="send" label="опубликовать" type="submit"/>
      </q-form>
    </div>
  </q-page>
  <div class="q-pa-lg flex flex-center">
  </div>
</template>

<script>
import {defineComponent, onMounted, ref} from 'vue'
import {useQuasar} from 'quasar'
import PostService from "src/service/post.service";
import Editor from '@tinymce/tinymce-vue'


export default defineComponent({
  name: 'NotePage',
  components: {
    'editor': Editor
  },
  setup() {

    const name = ref(null)
    const category = ref(null)
    const categories = ref(null)
    const preview = ref(null)
    const $q = useQuasar()
    const qeditor = ref(
      '<pre></pre>')
    const content = ref(null)

    const notify = (useQuasar) => {
      useQuasar.notify({
        color: 'negative',
        position: 'top',
        message: 'Loading failed',
        icon: 'report_problem'
      })
    }

    const onSubmit = () => {

      console.log(content.value)

      PostService.savePost({
        name: name.value,
        text: qeditor.value,
        category: {id: category.value.value},
        img: preview.value.name
      })
        .then((response) => {
          this.items = response.data
        })
        .catch(() => {
          $q.notify({
            color: 'negative',
            position: 'top',
            message: 'Loading failed',
            icon: 'report_problem'
          })
        })

    }

    const onReset = () => {
      /*      name.value = null
            age.value = null
            accept.value = false*/
    }

    onMounted(() => {

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
      onReset,
      qeditor,
      content,
    }
  }
})
</script>


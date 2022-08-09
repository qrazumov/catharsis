<template>
  <q-page class="flex-start q-pa-md">
    <div class="q-pa-md q-gutter-sm">
      <q-form
        class="q-gutter-md"
        @submit="onSubmit"
      >
        <p>Новая запись</p>
        <q-input
          @change="$emit('changeName', $event)"
          label="Название"
          :model-value="name"
        />
        <q-select
          @update:model-value="$emit('changeCategory', $event)"
          :emit-value="true"
          :options="categories"
          option-value="id"
          option-label="name"
          label="Категория"
          :model-value="category"
        />
        <q-input
          @change="$emit('changePreview', $event)"
          :model-value="preview"
          label="превью"
        />
        <q-input
          @selectionChange="$emit('changeVideo', $event)"
          :v-model="video"
          label="видео"
        />
        <editor
          @change="handleChange"
          v-if="visible"
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
          api-key="ybri4nrrhkarhhvpii1q7y66tjjqgvd3ckhjyjx7rv1g2u9s"
        />
        <q-btn  color="primary" icon="send" label="опубликовать" type="submit"/>
      </q-form>
    </div>
  </q-page>
</template>

<script>
import {defineComponent, ref, onBeforeMount} from 'vue'
import Editor from '@tinymce/tinymce-vue'
import { toRefs, toRef } from 'vue'


export default defineComponent({
  name: 'NotesTmpl',
  components: {
    Editor
  },
  props: {
    name: {
      type: String,
      required: true,
    },
    onSubmit: {
      type: Function,
      required: true,
    },
    category: {
      type: String,
      required: true,
    },
    cnt: {
      type: String,
      required: true,
    },
    categories: {
      type: Array,
      required: true,
    },
    preview: {
      type: String,
      required: false,
    },
    video: {
      type: String,
      required: true,
    },
    visible: {
      type: Boolean,
      required: true,
    },
  },
  emits: ['changeName', 'changeCategory', 'changePreview', 'changeVideo', 'changeContent'],


  setup (props, { emit }) {

    const content = toRef(props, 'cnt')

    const handleChange = (event) => {
      emit("changeContent", content.value)
    }



    return {
      handleChange,
      content
    }
  }


})
</script>


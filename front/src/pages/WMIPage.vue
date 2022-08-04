<template>
  <q-page class="flex-start q-pa-md">
    <div class="q-pa-md q-gutter-sm">
      <q-banner class="bg-grey-3" rounded>
        <template v-slot:avatar>
          <img
            src="https://i.ibb.co/DkLFN7v/ami.jpg"
            style="width: 200px;"
          >
        </template>
        <p>Разумов Александр, программист</p>
        <p>
          <q-icon
            color="primary"
            name="contact_support" size="24px"
          />
          телега 89017039620
        </p>
        <p>
          <q-icon
            color="red"
            name="code" size="24px"
          />
          гит github.com/qrazumov
        </p>
      </q-banner>
    </div>
  </q-page>
  <div class="q-pa-lg flex flex-center">
  </div>
</template>

<script>
import {defineComponent} from 'vue'
import {useMeta} from "quasar"

export default defineComponent({
  name: 'WMIPage',
  setup() {
    const metaData = {
      // sets document title
      title: 'Who am i',
      // optional; sets final title as "Index Page - My Website", useful for multiple level meta
      titleTemplate: title => `${title} - razymov.tech`,

      // meta tags
      meta: {
        description: { name: 'description', content: 'Who am i' },
        keywords: { name: 'keywords', content: 'Who am i' },
        equiv: { 'http-equiv': 'Content-Type', content: 'text/html; charset=UTF-8' },
      },
    }
    useMeta(metaData)

    return {}
  }
})
</script>


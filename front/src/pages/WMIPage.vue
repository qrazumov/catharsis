<template>
  <q-page class="flex-start q-pa-md">
    <div class="q-pa-md q-gutter-sm row justify-center">
      <q-card class="my-card col-md-4 col-lg-3 col-xs-12" flat square>
        <img src="http://razymov.tech:81/wl/?id=yq8q5lBEQBT2PbQAq6CO0m3gUaHQvSQ6&fmode=open">

        <q-card-section>
          <div class="text-h6">Разумов Александр, программист</div>
          <div class="text-subtitle2">
            <br/>
            <p>
              <q-icon
                color="primary"
                name="send" size="24px"
              />
              телега t.me/qrazumov
            </p>
            <p>
              <q-icon
                color="red"
                name="code" size="24px"
              />
              гит github.com/qrazumov
            </p>
          </div>
        </q-card-section>

        <q-card-section class="q-pt-none">
          {{ lorem }}
        </q-card-section>
      </q-card>

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
        description: {name: 'description', content: 'Who am i'},
        keywords: {name: 'keywords', content: 'Who am i'},
        equiv: {'http-equiv': 'Content-Type', content: 'text/html; charset=UTF-8'},
      },
    }
    useMeta(metaData)

    return {}
  }
})
</script>


<template>
  <q-page>
    <q-tabs
      v-model="tab"
      active-color="primary"
      align="justify"
      class="text-grey q-pa-md q-gutter-sm"
      dense
      indicator-color="primary"
      narrow-indicator
    >
      <q-tab label="парсер средней цены авто" name="parser"/>
      <q-tab label="цены в динамике" name="avg"/>
    </q-tabs>
    <q-tab-panels v-model="tab" animated>
      <q-tab-panel name="parser">
        <q-table
          :columns="columns"
          :rows="rows"
          :rows-per-page-options="[]"
          row-key="url"
          title="Парсер средних цен"
        >
          <template v-slot:body="props">
            <q-tr :props="props">
              <q-td key="url" :props="props">
                <q-input v-model="props.row.url" label="parse url"/>
                <q-popup-edit v-slot="scope" v-model="props.row.url" buttons>
                  <q-input v-model="scope.value" autofocus counter dense @keyup.enter="scope.set"/>
                </q-popup-edit>
              </q-td>

              <q-td key="median" :props="props"><span class="text-weight-bolder">{{ props.row.median }}</span></q-td>
              <q-td key="avg" :props="props"><span class="text-weight-bolder">{{ props.row.avg }}</span></q-td>
              <q-td key="min" :props="props"><span class="text-weight-bolder">{{ props.row.min }}</span></q-td>
              <q-td key="max" :props="props"><span class="text-weight-bolder">{{ props.row.max }}</span></q-td>
            </q-tr>
          </template>
          <template v-slot:top>
            <q-btn :disable="loading" color="primary" icon="add_circle" label="Добавить строку" size="md"
                   @click="addRow"/>
            <q-btn class="q-ml-sm" color="deep-orange" icon="bolt" label="Парсить!" size="md" @click="parseAction"/>
          </template>
        </q-table>
        <q-inner-loading :showing="visible">
          <q-spinner-gears color="primary" size="50px"/>
        </q-inner-loading>
      </q-tab-panel>
      <q-tab-panel name="avg">
        <p class="text-h6">в проработке...</p>
      </q-tab-panel>
    </q-tab-panels>
  </q-page>
</template>

<script>
import {defineComponent, ref} from 'vue'
import {useMeta, useQuasar} from "quasar"
import ParserService from "src/service/parser.service"

export default defineComponent({
  name: 'ParserPage',

  setup() {

    useMeta({
      title: "parser project",
      titleTemplate: title => `${title} - razymov.tech`,
      meta: {
        description: {name: 'description', content: "parser"},
        keywords: {name: 'keywords', content: "parser"},
        equiv: {'http-equiv': 'Content-Type', content: 'text/html; charset=UTF-8'},
      },
    })
    const $q = useQuasar()
    const parsURL = ref("https://auto.ru/cars/volkswagen/polo/20113124/all/")
    const result = ref("средняя цена")
    const visible = ref(false)
    const columns = [
      {name: 'url', required: true, label: 'url', align: 'left', field: 'url', sortable: true},
      {name: 'median', align: 'center', label: 'медиана', field: 'median', sortable: true},
      {name: 'avg', align: 'center', label: 'средняя', field: 'avg', sortable: true},
      {name: 'min', align: 'center', label: 'min', field: 'min', sortable: true},
      {name: 'max', align: 'center', label: 'max', field: 'max', sortable: true},
    ]
    const originalRows = [
      {
        url: 'https://auto.ru/cars/volkswagen/polo/20113124/all/',
        median: '-',
        avg: '-',
        min: '-',
        max: '-',
      }
    ]
    const rows = ref([...originalRows])
    const loading = ref(false)
    const notify = (useQuasar) => {
      useQuasar.notify({
        color: 'negative',
        position: 'top',
        message: 'Parse result failed',
        icon: 'report_problem'
      })
    }

    const parseAction = async () => {

      visible.value = true
      let arrURLs = []
      const formatter = new Intl.NumberFormat('ru-RU', {
        style: 'currency',
        currency: 'RUB',
      })

      for (let i = 0; i < rows.value.length; i++) {
        if (rows.value[i].url !== null)
          arrURLs.push(rows.value[i].url)
      }
      for (let i = 0; i < arrURLs.length; i++) {
        try {
          let axiosResponse = await ParserService.postParser({parserurl: arrURLs[i]})
          if (axiosResponse.data.error !== null) {
            notify($q)
          } else {
            rows.value[i].median = formatter.format(axiosResponse.data.result.median)
            rows.value[i].avg = formatter.format(axiosResponse.data.result.avg)
            rows.value[i].min = formatter.format(axiosResponse.data.result.min)
            rows.value[i].max = formatter.format(axiosResponse.data.result.max)
          }
        } catch (e) {
          notify($q)
        }
      }
      visible.value = false

    }
    const addRow = () => {
      loading.value = true
      const
        row = {
          url: null,
          median: '-',
          avg: '-',
          min: '-',
          max: '-',
        }

      rows.value.push(row)
      loading.value = false
    }
    const greet = () => {

      visible.value = true
      let startTime = performance.now()

      ParserService.postParser({parserurl: parsURL.value})
        .then((response) => {

          if (response.data.error) {
            result.value = "Ошибка парсинга"
            return
          }

          var endTime = performance.now()
          console.log(response.data.result)

          result.value = JSON.stringify(response.data.result)
          visible.value = false
          $q.dialog({
            title: `Средняя цена [${Math.round((endTime - startTime) / 1000)}s]`,
            message: result.value
          }).onOk(() => {
          }).onCancel(() => {
          }).onDismiss(() => {
          })
        })
        .catch(() => {
          visible.value = false
          $q.notify({
            color: 'negative',
            position: 'top',
            message: 'Loading failed',
            icon: 'report_problem'
          })
        })
    }

    return {
      parsURL,
      greet,
      result,
      visible,
      tab: ref('parser'),
      columns,
      rows: ref(rows),
      loading,
      addRow,
      parseAction,
    }

  }

})
</script>


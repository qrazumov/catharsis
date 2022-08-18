<template>
  <q-page>
    <div class="q-pa-md q-gutter-sm">
      <q-tabs
        v-model="tab"
        active-color="primary"
        align="justify"
        class="text-grey"
        dense
        indicator-color="primary"
        narrow-indicator
      >
        <q-tab label="средняя цена" name="avg"/>
        <q-tab label="парсер авто.ру" name="parser"/>
      </q-tabs>
      <q-separator/>
      <q-tab-panels v-model="tab" animated>
        <q-tab-panel name="avg">
          <div class="q-pa-md q-gutter-sm">
            <q-input v-model="parsurl" label="введите url для парсинга"/>
            <q-btn color="primary" label="узнать среднюю цену" size="md" @click="greet"/>
          </div>
          <q-inner-loading :showing="visible">
            <q-spinner-gears color="primary" size="50px"/>
          </q-inner-loading>
        </q-tab-panel>
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
                <q-td key="checked" :props="props">
                  <div class="q-pa-md">
                    <q-checkbox v-model="props.row.checked" @click="tgl"/>
                  </div>
                </q-td>
                <q-td style="width: 20px" key="url" :props="props">
                  {{ props.row.url }}
                  <q-popup-edit v-slot="scope" v-model="props.row.url" buttons>
                    <q-input  v-model="scope.value" autofocus counter dense @keyup.enter="scope.set"/>
                  </q-popup-edit>
                </q-td>

                <q-td key="median" :props="props"><span class="text-weight-bolder">{{ props.row.median }}</span></q-td>
                <q-td key="avg" :props="props"><span class="text-weight-bolder">{{ props.row.avg }}</span></q-td>
                <q-td key="min" :props="props"><span class="text-weight-bolder">{{ props.row.min }}</span></q-td>
                <q-td key="max" :props="props"><span class="text-weight-bolder">{{ props.row.max }}</span></q-td>
              </q-tr>
            </template>
            <template v-slot:top>
              <q-btn :disable="loading" color="primary" label="Add row" @click="addRow"/>
              <q-btn :disable="loading" class="q-ml-sm" color="primary" label="Remove row" @click="removeRow"/>
              <q-btn :disable="hasParse" class="q-ml-sm" color="secondary" label="Parse!" @click="parseAction"/>
            </template>
          </q-table>
          <q-inner-loading :showing="visible">
            <q-spinner-gears color="primary" size="50px"/>
          </q-inner-loading>
        </q-tab-panel>
      </q-tab-panels>
    </div>
  </q-page>
</template>

<script>
import {defineComponent, ref} from 'vue'

import {useMeta, useQuasar} from "quasar"
import ParserService from "src/service/parser.service"
import lodash from "lodash"

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
    const parsurl = ref("https://auto.ru/cars/volkswagen/polo/20113124/all/")
    const result = ref("средняя цена")
    const visible = ref(false)
    const greet = () => {

      visible.value = true
      let startTime = performance.now()

      ParserService.postParser({parserurl: parsurl.value})
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
    const columns = [
      {name: 'checked', align: 'center', label: 'checked', field: 'checked', sortable: true},
      {name: 'url', required: true, label: 'url для парсинга', align: 'left', field: 'url', sortable: true},
      {name: 'median', align: 'center', label: 'median', field: 'median', sortable: true},
      {name: 'avg', align: 'center', label: 'avg', field: 'avg', sortable: true},
      {name: 'min', align: 'center', label: 'min', field: 'min', sortable: true},
      {name: 'max', align: 'center', label: 'max', field: 'max', sortable: true},
    ]
    const originalRows = [
      {
        checked: false,
        url: 'https://auto.ru/cars/volkswagen/polo/20113124/all/',
        median: '-',
        avg: '-',
        min: '-',
        max: '-',
      }
    ]
    const rows = ref([...originalRows])
    const loading = ref(false)
    const tgl = () => {hasParse.value = false}

    const parseAction = async () => {

      console.log(rows.value)
      let b = []
      for (let i = 0; i < rows.value.length; i++) {
        b.push(rows.value[i].url)
      }
      console.log(b)

      visible.value = true

      // Create our number formatter.
      var formatter = new Intl.NumberFormat('ru-RU', {
        style: 'currency',
        currency: 'RUB',

        // These options are needed to round to whole numbers if that's what you want.
        //minimumFractionDigits: 0, // (this suffices for whole numbers, but will print 2500.10 as $2,500.1)
        //maximumFractionDigits: 0, // (causes 2500.99 to be printed as $2,501)
      });

      for (let i = 0; i < b.length; i++) {

        let rr = await ParserService.postParser({parserurl: b[i]})
        rows.value[i].median = formatter.format(rr.data.result.median)
        rows.value[i].avg = formatter.format(rr.data.result.avg)
        rows.value[i].min = formatter.format(rr.data.result.min)
        rows.value[i].max = formatter.format(rr.data.result.max)

      }
      visible.value = false

      console.log(rows.value)

    }
    const hasParse = ref(true)

    return {
      parsurl,
      greet,
      result,
      visible,
      tab: ref('parser'),
      columns,
      rows: ref(rows),
      loading,
      addRow() {
        loading.value = true
        setTimeout(() => {
          const
            row = {
              checked: false,
              url: 'insert url...',
              median: '-',
              avg: '-',
              min: '-',
              max: '-',
            }

          rows.value.push(row)
          loading.value = false
        }, 500)
      },
      removeRow() {
        loading.value = true
        setTimeout(() => {
          const index = Math.floor(Math.random() * rows.value.length)
          rows.value = [...rows.value.slice(0, index), ...rows.value.slice(index + 1)]
          loading.value = false
        }, 500)
      },
      tgl,
      parseAction,
      hasParse,
    }

  }

})
</script>


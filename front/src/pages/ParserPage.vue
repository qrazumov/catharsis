<template>
  <q-page>
    <div class="q-pa-md q-gutter-sm">
      <q-banner class="bg-light-blue-4 text-white">
        Авто.ру парсер
        <br/>
        Частота обновления статистики: раз в 12 часов
        <br/>
        Глубина страниц: 3
      </q-banner>
      <q-input v-model="parsurl" label="введите url для парсинга" />
      <q-btn @click="greet" color="primary" label="узнать среднюю цену" size="md" />
      <q-spinner-gears
        v-if="visible"
        color="secondary"
        size="3em"
      />
      <br />
      <q-badge class="text-weight-bolder text-h6" color="orange" text-color="black" :label="result" />
    </div>
    <div class="q-pa-md row justify-center">
      <Bar
        v-for="itm in chartData"
        :key="itm"
        :chart-data="itm"
        :chart-id="chartId"
        :chart-options="chartOptions"
        class="col-md-9 col-lg-9 col-sm-9 col-xs-12"
        height="200"
      />
    </div>
  </q-page>
</template>

<script>
import {defineComponent, ref} from 'vue'

import {useMeta, useQuasar} from "quasar"
import ParserService from "src/service/parser.service"
import {Bar} from 'vue-chartjs'
import {BarElement, CategoryScale, Chart as ChartJS, Legend, LinearScale, Title, Tooltip} from 'chart.js'
import lodash from 'lodash'

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale)

export default defineComponent({
  name: 'ParserPage',
  components: {
    Bar
  },

  setup() {

    const chartData = ref([])
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
    const backgroundColor = [
      'rgba(255, 99, 132, 0.2)',
      'rgba(255, 159, 64, 0.2)',
      'rgba(255, 205, 86, 0.2)',
      'rgba(75, 192, 192, 0.2)',
      'rgba(54, 162, 235, 0.2)',
      'rgba(153, 102, 255, 0.2)',
      'rgba(201, 203, 207, 0.2)'
    ]
    const borderColor = [
      'rgb(255, 99, 132)',
      'rgb(255, 159, 64)',
      'rgb(255, 205, 86)',
      'rgb(75, 192, 192)',
      'rgb(54, 162, 235)',
      'rgb(153, 102, 255)',
      'rgb(201, 203, 207)'
    ]
    const topBorder = 1_000_000;

    ParserService.getParser()
      .then((response) => {

        const r = lodash.groupBy(response.data, 'good')
        for (let key of Object.keys(r)) {
          const res = []
          const lb = []
          const delta = Math.floor(r[key].length / 7)
          for (let i = 0; i < r[key].length; i += delta) {
            res.push(r[key][i].price)
            let date = new Date(r[key][i].createdAt);
            let dt = date.getDay() + '.' + date.getMonth() + '.' + date.getFullYear().toString().slice(2) + ' ' + date.getHours() + ':' + date.getMinutes();
            lb.push(dt)
          }
          res.push(topBorder)
          chartData.value.push({
            labels: lb,
            datasets: [
              {
                label: 'Авто.ру: средние цены на ' + r[key][0].good,
                backgroundColor: backgroundColor,
                borderColor: borderColor,
                borderWidth: 1,
                data: res
              }
            ],
            maintainAspectRatio: false,

          })
        }
      })
      .catch(() => {
        $q.notify({
          color: 'negative',
          position: 'top',
          message: 'Loading failed',
          icon: 'report_problem'
        })
      })

    const parsurl = ref("https://auto.ru/cars/volvo/s40/4602593/all/?displacement_from=2400&displacement_to=2400&sort=cr_date-desc")
    const result = ref("средняя цена")
    const visible = ref(false)

    const greet = () => {
      visible.value = true
      ParserService.postParser({parserurl: parsurl.value})
        .then((response) => {
          result.value = response.data + ' руб'
          visible.value = false
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
      chartData,
      chartOptions: {
        responsive: true
      },
      width: 300,
      height: 300,
      chartId: 'Авто.ру парсер',
      parsurl,
      greet,
      result,
      visible

    }

  }

})
</script>


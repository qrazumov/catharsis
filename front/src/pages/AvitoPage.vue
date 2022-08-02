<template>
  <q-page>
    <div class="q-pa-md q-gutter-sm">
      <q-banner class="bg-primary text-white">
        Авито парсер: видекарта nvidia gtx 1660 super
        <br/>
        урл:
        https://www.avito.ru/moskva/tovary_dlya_kompyutera/komplektuyuschie/videokarty-ASgBAgICAkTGB~pm7gmmZw?cd=1&q=gtx+1660+super
        <br/>
        Частота обновления статистики: 30 сек
        <br/>
        Глубина страниц: 3
      </q-banner>
    </div>
    <div class="q-pa-md row justify-center">
      <Bar
        :chart-data="chartData"
        :chart-id="chartId"
        :chart-options="chartOptions"
        class="col-9"
      />
    </div>
  </q-page>
</template>

<script>
import {defineComponent, onMounted, ref} from 'vue'

import {useQuasar} from "quasar";
import AvitoService from "src/service/avito.service";

import {Bar} from 'vue-chartjs'
import {BarElement, CategoryScale, Chart as ChartJS, Legend, LinearScale, Title, Tooltip} from 'chart.js'

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale)

export default defineComponent({
  name: 'AvitoPage',
  components: {
    Bar
  },

  setup() {

    const chartData = ref({

      labels: [],
      datasets: [
        {
          label: 'Авито парсер',
          backgroundColor: [
            'rgba(255, 99, 132, 0.2)',
            'rgba(255, 159, 64, 0.2)',
            'rgba(255, 205, 86, 0.2)',
            'rgba(75, 192, 192, 0.2)',
            'rgba(54, 162, 235, 0.2)',
            'rgba(153, 102, 255, 0.2)',
            'rgba(201, 203, 207, 0.2)'
          ],
          borderColor: [
            'rgb(255, 99, 132)',
            'rgb(255, 159, 64)',
            'rgb(255, 205, 86)',
            'rgb(75, 192, 192)',
            'rgb(54, 162, 235)',
            'rgb(153, 102, 255)',
            'rgb(201, 203, 207)'
          ],
          borderWidth: 1,
          data: []
        }
      ],
      maintainAspectRatio: false,
      scales: {
        xAxes: [
          {
            stacked: true
          }
        ],
        yAxes: [
          {
            stacked: true
          }
        ]
      }

    })

    onMounted(() => {

      const $q = useQuasar()

      AvitoService.getAvito()
        .then((response) => {
          const res = []
          const lb = []
          const delta = Math.floor(response.data.length / 5)
          for (let i = 0; i < response.data.length; i += delta) {
            res.push(response.data[i].price)
            lb.push(response.data[i].createdAt)
          }
          chartData.value.datasets[0].data = res
          chartData.value.labels = lb
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
      chartData,
      chartOptions: {
        responsive: true
      },
      width: 300,
      height: 300,
      chartId: 'Авито парсер'

    }

  }

})
</script>


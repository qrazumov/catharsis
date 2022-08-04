<template>
  <q-page>
    <q-infinite-scroll ref="infPost" :offset="250" @load="onLoad">
      <div
        v-if="items.length > 0"
        class="q-pa-md q-col-gutter-lg row">
        <div
          v-for="item in items"
          :key="item.id"
          class="my-card col-lg-6 col-md-6 col-sm-12 col-xs-12"
        >
          <q-parallax
            :height="150"
            :src="item.img"
          />
          <q-card-section>
            <div class="text-h6">{{ item.name }}</div>
            <div class="text-subtitle2">
              <q-badge color="blue">
                {{ item.category.name }}
              </q-badge>
            </div>
          </q-card-section>

          <q-card-section class="q-pt-none">
            <div v-html="item.text"></div>
          </q-card-section>
          <q-card-actions class="fit row wrap justify-end items-start content-start">
            <q-btn flat icon="event" round/>
            <q-btn flat>
              {{ item.createdAt }}
            </q-btn>
            <q-btn :href="/post/ + item.id" :label="'читать id ' + item.id" color="primary" />
          </q-card-actions>
        </div>
      </div>
      <div
        v-else
        class="q-pa-md q-col-gutter-lg row">
        <q-card
          v-for="item in Array(4)"
          :key="item"
          class="my-card col-lg-6 col-md-6 col-sm-12 col-xs-12"
        >
          <q-skeleton height="200px" square/>
          <q-card-section>
            <div class="text-h6">
              <q-skeleton type="text"/>
            </div>
          </q-card-section>

          <q-card-section class="q-pt-none">
            <q-skeleton type="text"/>
          </q-card-section>
          <q-card-actions>
            <q-skeleton type="QBtn"/>
          </q-card-actions>
        </q-card>
      </div>
      <template v-slot:loading>
        <div class="row justify-center q-my-md">
          <q-spinner-dots color="primary" size="40px"/>
        </div>
      </template>
    </q-infinite-scroll>
  </q-page>
</template>

<script>
import {defineComponent, ref} from 'vue'

export default defineComponent({
  name: 'PostsTmpl',
  props: {
    items: {
      type: Array,
      required: true,
    },
    onLoad: {
      type: Function,
      required: true,
    }
  },
  setup() {
    const infPost = ref(null)
    return {
      infPost
    };
  }
})
</script>


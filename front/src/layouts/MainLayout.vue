<template>
  <q-layout id="main-layout" view="lHr lpR fFf" container>
    <q-header class="bg-blue-grey-10" elevated>
      <q-toolbar>
        <q-btn
          aria-label="Menu"
          dense
          flat
          icon="menu"
          round
          @click="toggleLeftDrawer"
        />
        <q-toolbar-title>
          <span class="text-deep-orange-10">r</span>
          <span class="text-orange-3">a</span>
          <span class="text-green-3">z</span>
          <span class="text-cyan-3">y</span>
          <span class="text-light-blue-3">m</span>
          <span class="text-deep-purple-3">o</span>
          <span class="text-orange-3">v</span>.tech <span class="text-weight-thin text-caption">{ dev blog }</span>
        </q-toolbar-title>
        <div>blog v{{ version }}</div>
      </q-toolbar>
    </q-header>

    <q-drawer
      v-model="leftDrawerOpen"
      bordered
      show-if-above
    >
      <q-list>
        <q-item-label
          header
        >
          for i to n
        </q-item-label>

        <EssentialLink
          v-for="link in essentialLinks"
          :key="link.title"
          v-bind="link"
        />
      </q-list>
    </q-drawer>
    <q-page-container>
      <router-view/>
    </q-page-container>
  </q-layout>
</template>

<script>
import {defineComponent, ref} from 'vue'
import EssentialLink from 'components/EssentialLink.vue'
import packageInfo from "../../package.json";

const linksList = [
  {
    title: 'index',
    caption: 'razymov.ru',
    icon: 'exposure_zero',
    link: '/'
  },
  {
    title: 'category',
    caption: 'blog store',
    icon: 'data_object',
    link: '#/category'
  },
  {
    title: 'map',
    caption: 'map site',
    icon: 'menu_book',
    link: '#/map'
  },
  {
    title: 'about me',
    caption: 'whoami',
    icon: 'account_circle',
    link: '#/whoami'
  },
  {
    title: 'парсер',
    caption: 'парсер средних цен',
    icon: 'code',
    link: '#/project/parser',
    separator: true
  }
]

export default defineComponent({
  name: 'MainLayout',

  components: {
    EssentialLink
  },

  setup() {
    const leftDrawerOpen = ref(false)

    return {
      essentialLinks: linksList,
      leftDrawerOpen,
      toggleLeftDrawer() {
        leftDrawerOpen.value = !leftDrawerOpen.value
      },
      version: packageInfo.version
    }
  }
})
</script>

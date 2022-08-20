<template>
  <div></div>
</template>

<script>
import {defineComponent} from "vue"
import {useAuthStore} from "stores/auth"
import {useRouter} from 'vue-router'

export default defineComponent({
  name: 'LogoutPage',
  setup() {
    const $router = useRouter()
    const store = useAuthStore()

    store.logout()
    $router.push('/')

    return {}
  }
})
</script>

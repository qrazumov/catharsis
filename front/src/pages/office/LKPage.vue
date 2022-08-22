<template>
  <div class="q-pa-md row">
    <div>
      <p class="text-h6">Личный кабинет</p>
      <p>id: {{ id }}</p>
      <p>email: {{ email }}</p>
    </div>
  </div>
</template>

<script>
import {useQuasar} from "quasar"
import {defineComponent, onMounted, ref} from "vue"
import {useUserStore} from "stores/user"
import {useRouter} from 'vue-router'
import UserService from "src/service/auth/user.service"

export default defineComponent({
  name: 'LKPage',
  setup() {
    const $q = useQuasar()
    const store = useUserStore()
    const $router = useRouter()
    const id = ref(null)
    const email = ref(null)


    onMounted(() => {
      if (!store.isAuth) {
        $router.push('/office/login');
      }
    })

    UserService.getInfo().then((res) => {
      id.value = res.data.id
      email.value = res.data.email
    })


    return {
      store,
      id,
      email

    }
  }
})
</script>

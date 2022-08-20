<template>
  <div class="q-pa-md row justify-center">
    <div>
      <h6>Регистрация личного кабинета:</h6>
      <q-form
        class="q-gutter-md col-4"
        @submit="onSubmit"
      >
        <q-input
          v-model="user.username"
          :rules="[ val => val && val.length > 0 || 'Пожалуйста, введите почту']"
          filled
          label="Почта *"
          lazy-rules
          type="email"
        />
        <q-input
          v-model="user.password"
          :rules="[
          val => val !== null && val !== '' || 'Пожалуйста, введите пароль'
        ]"
          filled
          label="Пароль *"
          lazy-rules
          type="password"
        />
        <div>
          <q-btn color="primary" label="Регистрация" type="submit"/>
        </div>
      </q-form>
    </div>
  </div>
</template>

<script>
import {useQuasar} from "quasar"
import {defineComponent, ref} from "vue"
import {useRouter} from "vue-router"
import {useAuthStore} from "stores/auth"

export default defineComponent({
  name: 'RegisterPage',
  setup() {
    const $q = useQuasar()

    const loading = ref(false)
    const message = ref("")
    const store = useAuthStore()
    const user = ref({
      username: null,
      password: null
    })
    const $router = useRouter()

    return {
      store,
      loading,
      message,
      user,

      onSubmit() {

        store.register(user.value).then(
          () => {
            $router.push('/office/id')
            $q.notify({
              color: 'green-4',
              textColor: 'white',
              icon: 'cloud_done',
              message: 'Вы зарегистрировались в лк'
            })
          },
          (error) => {
            $q.notify({
              color: 'negative',
              position: 'top',
              message: 'Loading failed',
              icon: 'ошибка регистрации'
            })
            loading.value = false;
            message.value =
              (error.response &&
                error.response.data &&
                error.response.data.message) ||
              error.message ||
              error.toString();
          }
        );
      },
    }
  }
})
</script>

<style>
.q-card {
  width: 360px;
}
</style>

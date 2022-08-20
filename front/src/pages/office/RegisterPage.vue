<template>
    <div class="q-pa-md row justify-center">
      <div>
        <h6>Регистрация личного кабинета:</h6>
        <q-form
          @submit="onSubmit"
          class="q-gutter-md col-4"
        >
          <q-input
            type="email"
            filled
            v-model="user.username"
            label="Почта *"
            lazy-rules
            :rules="[ val => val && val.length > 0 || 'Пожалуйста, введите почту']"
          />
          <q-input
            filled
            type="password"
            v-model="user.password"
            label="Пароль *"
            lazy-rules
            :rules="[
          val => val !== null && val !== '' || 'Пожалуйста, введите пароль'
        ]"
          />
          <div>
            <q-btn label="Регистрация" type="submit" color="primary"/>
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
  setup () {
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

      onSubmit () {

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

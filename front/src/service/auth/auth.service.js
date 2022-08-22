import axios from 'axios';
import {useUserStore} from "stores/user"
import {API_AUTH, GATE_WAY} from "src/config/appconfig"
import { Cookies } from 'quasar'


const API_URL_LOGIN = GATE_WAY + API_AUTH + 'login'
const API_URL_REGISTER = GATE_WAY + API_AUTH + 'register'

class AuthService {
  login(user) {
    return axios
      .post(API_URL_LOGIN, {
        email: user.username,
        password: user.password
      })
      .then(response => {
        if (response.data["jwt-token"]) {
          const store = useUserStore()
          Cookies.set('user', JSON.stringify(response.data), {
            expires: '60m'
          })
          store.isAuth = true
        }

        return response.data;
      });
  }

  logout() {
    const store = useUserStore()
    store.isAuth = false
    Cookies.remove('user')
  }

  register(user) {
    return axios.post(API_URL_REGISTER, {
      email: user.username,
      password: user.password
    }).then(response => {
      if (response.data["jwt-token"]) {
        const store = useUserStore()
        Cookies.set('user', JSON.stringify(response.data), {
          expires: 10
        })
        store.isAuth = true
      }

      return response.data;
    });
  }
}

export default new AuthService();

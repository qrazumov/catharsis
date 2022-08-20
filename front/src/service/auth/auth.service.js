import axios from 'axios';
import {useUserStore} from "stores/user"
import {GATE_WAY, API_AUTH} from "src/config/appconfig"

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
          store.user = JSON.stringify(response.data)
        }

        return response.data;
      });
  }

  logout() {
    const store = useUserStore()
    store.user = null
  }

  register(user) {
    return axios.post(API_URL_REGISTER, {
      email: user.username,
      password: user.password
    }).then(response => {
      if (response.data["jwt-token"]) {
        const store = useUserStore()
        store.user = JSON.stringify(response.data)
      }

      return response.data;
    });
  }
}

export default new AuthService();

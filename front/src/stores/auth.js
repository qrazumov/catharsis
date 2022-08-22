import {defineStore} from 'pinia'
import AuthService from '../service/auth/auth.service'

export const useAuthStore = defineStore('auth', {
  state: () => ({
  }),

  getters: {},

  actions: {

    login(user) {
      return AuthService.login(user).then(
        user => {
          return Promise.resolve(user);
        },
        error => {
          return Promise.reject(error);
        }
      );
    },
    logout() {
      AuthService.logout();
    },
    register(user) {
      return AuthService.register(user).then(
        response => {
          return Promise.resolve(response.data);
        },
        error => {
          return Promise.reject(error);
        }
      );
    }

  }
})

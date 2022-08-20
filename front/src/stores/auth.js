import { defineStore } from 'pinia'
import AuthService from '../service/auth/auth.service'
import {useUserStore} from "stores/user"

const userStore = useUserStore()
const user = userStore.user
const initialState = user
  ? { status: { loggedIn: true }, user }
  : { status: { loggedIn: false }, user: null };

export const useAuthStore = defineStore('auth', {
  state: () => ({
    initialState,
  }),

  getters: {},

  actions: {

    login(user) {
      return AuthService.login(user).then(
        user => {
          this.initialState.status.loggedIn = true;
          this.initialState.user = user;
          return Promise.resolve(user);
        },
        error => {
          this.initialState.status.loggedIn = false;
          return Promise.reject(error);
        }
      );
    },
    logout() {
      AuthService.logout();
      this.initialState.status.loggedIn = false;
      this.initialState.user = null;
    },
    register(user) {
      return AuthService.register(user).then(
        response => {
          this.initialState.status.loggedIn = false;
          return Promise.resolve(response.data);
        },
        error => {
          this.initialState.status.loggedIn = false;
          return Promise.reject(error);
        }
      );
    }

  }
})

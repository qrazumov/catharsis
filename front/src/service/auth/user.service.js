import axios from 'axios';
import authHeader from './auth-header';
import {GATE_WAY} from "src/config/appconfig"

const API_URL = GATE_WAY + '/api/user/info';

class UserService {

  getInfo() {
    return axios.get(API_URL, {headers: authHeader()});
  }

}

export default new UserService();

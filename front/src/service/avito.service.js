import {api} from "boot/axios";
import {API_POINT} from '../config/appconfig'

class AvitoService {

  getAvito() {
    return api.get(API_POINT + `avito`)
  }
}

export default new AvitoService()

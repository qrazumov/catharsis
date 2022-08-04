import {api} from "boot/axios";
import {API_POINT} from '../config/appconfig'

class ParserService {

  getParser() {
    return api.get(API_POINT + `parser`)
  }
}

export default new ParserService()

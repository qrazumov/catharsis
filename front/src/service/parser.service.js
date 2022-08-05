import {api} from "boot/axios";
import {API_POINT} from '../config/appconfig'

class ParserService {

  getParser() {
    return api.get(API_POINT + `parser`)
  }
  postParser(parserurl) {
    return api.post(API_POINT + `parser`, parserurl)
  }
}

export default new ParserService()

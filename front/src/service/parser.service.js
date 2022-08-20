import {api} from "boot/axios";
import {API_POINT} from '../config/appconfig'
import authHeader from '../service/auth/auth-header';


class ParserService {

  getParser() {
    return api.get(API_POINT + `parser`)
  }

  postParser(parserurl) {
    return api.post(API_POINT + `parser`, parserurl, {headers: authHeader()})
  }
}

export default new ParserService()

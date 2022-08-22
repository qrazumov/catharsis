import { Cookies } from 'quasar'


export default function authHeader() {
  const user = Cookies.get('user')

  if (user && user["jwt-token"]) {
    return {Authorization: 'Bearer ' + user["jwt-token"]}; // for Spring Boot back-end
    // return { 'x-access-token': user.accessToken };       // for Node.js Express back-end
  } else {
    return {};
  }
}

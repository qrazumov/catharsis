import {api} from "boot/axios"
import {API_POINT} from 'src/config/appconfig'
import authHeader from 'src/service/auth/auth-header'

class PostService {

  getPosts(offset, limit) {
    return api.get(API_POINT + `posts?offset=${offset}&limit=${limit}`)
  }

  getPostsByCategory(categoryId, offset, limit) {
    return api.get(API_POINT + `posts/categories/${categoryId}/?offset=${offset}&limit=${limit}`)
  }

  getPost(id) {
    return api.get(API_POINT + `posts/${id}`)
  }

  patchPost(id, body) {
    return api.patch(API_POINT + `posts/${id}`, body, {headers: authHeader()})
  }

  getCategories() {
    return api.get(API_POINT + `categories`)
  }

  savePost(body) {
    return api.post(API_POINT + `posts`, body, {headers: authHeader()})
  }
}

export default new PostService()

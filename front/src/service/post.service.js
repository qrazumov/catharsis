import {api} from "boot/axios";
import {API_POINT} from '../config/appconfig'

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
    return api.patch(API_POINT + `posts/${id}`, body)
  }

  getCategories() {
    return api.get(API_POINT + `categories`)
  }

  savePost(body) {
    return api.post(API_POINT + `posts`, body)
  }
}

export default new PostService()

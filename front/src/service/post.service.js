import {api} from "boot/axios";
import {API_POINT} from '../config/appconfig'

class PostService {

  getPosts(offset, limit) {
    return api.get(API_POINT + `posts?offset=${offset}&limit=${limit}`)
  }

  getPosts() {
    return api.get(API_POINT + `posts`)
  }

  getPostsByCategory(categoryId, offset, limit) {
    return api.get(API_POINT + `posts/categories/${categoryId}/?offset=${offset}&limit=${limit}`)
  }

  getPost(id) {
    return api.get(API_POINT + `posts/${id}`)
  }

  getCategories() {
    return api.get(API_POINT + `categories`)
  }

  savePost(body) {
    return api.post(API_POINT + `posts`, body)
  }
}

export default new PostService()

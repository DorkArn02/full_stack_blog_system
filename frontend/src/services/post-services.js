import axios from 'axios';
import authHeader from './auth-header';
import authServices from './auth-services';

const API_URL = '/api/posts/';

class PostService {
    /**
     * Get all posts
     */
    async getAllPosts() {
        const response = await axios.get(API_URL)
        const result = await response.data
        return result;
    }
    /**
     * Get post by id
     * @param {UUID} id 
     */
    async getPostById(id) {
        const response = await axios.get(API_URL + id)
        const result = await response.data
        return result;
    }
    /**
     * Get posts by title
     * @param {String} title 
     */
    async getPostByTitle(title) {
        const response = await axios.get(API_URL + "search", { params: { title: title } })
        const result = await response.data
        return result;
    }
    /**
     * Create a new post
     * @param {String} title 
     * @param {String} content 
     */
    async addNewPost(title, content) {
        if (authServices.isTokenExpired()) {
            window.location.href = "/"
        } else {
            return await axios.post(API_URL, { title, content }, { headers: authHeader() });
        }
    }
    /**
     * Upload a picture to the specified post
     * @param {UUID} id 
     * @param {MultipartFile} image 
     */
    async addNewImage(id, image) {
        if (authServices.isTokenExpired()) {
            window.location.href = "/"
        } else {
            let file = new FormData()
            file.append('file', image)
            return await axios.post(API_URL + `/picture/${id}`, file, { headers: authHeader(), 'content-type': 'multipart/form-data' });
        }
    }
    /**
     * Delete a specified post by id
     * @param {UUID} id 
     */
    deletePostById(id) {
        if (authServices.isTokenExpired()) {
            window.location.href = "/"
        } else {
            return axios.delete(API_URL + id, { headers: authHeader() }).then(() => window.location.reload());
        }
    }
    /**
     * Update post by id
     * @param {UUID} id 
     * @param {Object} data 
     */
    updatePostById(id, data) {
        if (authServices.isTokenExpired()) {
            window.location.href = "/"
        } else {
            return axios.put(API_URL + id, data, { headers: authHeader() }).then(() => window.location.reload());
        }
    }
}
export default new PostService();
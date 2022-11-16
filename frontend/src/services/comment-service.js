import axios from 'axios';
import authHeader from './auth-header';
import authServices from './auth-services';

const API_URL = '/api/posts/comments/';

class CommentService {
    getCommentById(id) {
        return axios.get(API_URL + id);
    }

    async addNewComment(postId, data) {
        if (authServices.isTokenExpired()) {
            window.location.href("/")
        } else {
            return await axios.post(API_URL + postId, { content: data }, { headers: authHeader() }).then(() => window.location.reload());
        }
    }

    async deleteCommentById(postId) {
        if (authServices.isTokenExpired()) {
            window.location.href("/")
        } else {
            return await axios.delete(API_URL + postId, { headers: authHeader() }).then(() => window.location.reload());
        }
    }

    updateCommentById(postId, data) {
        if (authServices.isTokenExpired()) {
            window.location.href("/")
        } else {
            return axios.put(API_URL + postId, data, { headers: authHeader() });
        }
    }
}

export default new CommentService();
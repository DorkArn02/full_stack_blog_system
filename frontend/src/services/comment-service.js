import axios from 'axios';
import authHeader from './auth-header';
import authServices from './auth-services';

const API_URL = '/api/comments/';

class CommentService {
    getCommentById(id) {
        return axios.get(API_URL + id);
    }

    addNewComment(postId, data) {
        if (authServices.isTokenExpired()) {
            window.location.href("/")
        } else {
            return axios.post(API_URL + postId, data, { headers: authHeader() });
        }
    }

    deleteCommentById(postId) {
        if (authServices.isTokenExpired()) {
            window.location.href("/")
        } else {
            return axios.delete(API_URL + postId, { headers: authHeader() });
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
import axios from 'axios';
import authHeader from './auth-header';
import authServices from './auth-services';

const API_URL = '/api/users/';

class UserService {

    getUserById(userId) {
        return axios.get(API_URL + userId);
    }

    deleteUserById(userId) {
        return axios.delete(API_URL + userId, { headers: authHeader() });
    }

    updateUserById(userId, data) {
        return axios.put(API_URL + userId, data, { headers: authHeader() });
    }

    async getProfilePictureById(userId) {
        const response = await axios.get(API_URL + "profile/" + userId)
        const result = await response.data
        return result
    }

    async setProfilePictureById(userId, image) {
        if (authServices.isTokenExpired()) {
            window.location.href = "/"
        } else {
            let f = new FormData()
            f.append('file', image)

            return await axios.post(API_URL + "profile/" + userId, f, { headers: authHeader(), 'content-type': 'multipart/form-data' })
        }
    }
}

export default new UserService();
import axios from "axios";

const API_URL = "/api/auth/";


class AuthService {
    instance = axios.create({
        baseURL: "http://127.0.0.1:8080/api/auth/",
    });
    login(data) {
        return this.instance
            .post("login", {
                username: data.username,
                password: data.password
            })
            .then(response => {
                let user = {
                    accessToken: response.headers['authorization'],
                    user_id: response.data.user_id,
                    username: response.data.username,
                    email: response.data.email
                }
                if (response.headers['authorization']) {
                    localStorage.setItem("user", JSON.stringify(user));
                    window.location.reload()
                }
                return response.data;
            });
    }

    logout() {
        localStorage.removeItem("user");
    }

    register(data) {
        return axios.post(API_URL + "register", {
            username: data.username,
            email: data.email,
            password: data.password
        });
    }

    getCurrentUser() {
        return JSON.parse(localStorage.getItem('user'));;
    }

    getTokenDetails() {
        if (localStorage.getItem('user') != null) {
            return JSON.parse(atob(this.getCurrentUser().accessToken.split('.')[1]))
        }
    }

    isTokenExpired() {
        if (localStorage.getItem('user') != null) {

            const iat = new Date(this.getTokenDetails().iat * 1000)
            const exp = new Date(this.getTokenDetails().exp * 1000)

            if (Date.now() > exp) {
                return true
            }
            return false
        } else {
            return true;
        }
    }

}

export default new AuthService();
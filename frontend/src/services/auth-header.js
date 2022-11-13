/**
 * If the user logged in, then gets the bearer authorization token from the local storage
 * @returns Bearer authorization token
 */
export default function authHeader() {
    const user = JSON.parse(localStorage.getItem('user'));

    if (user && user.accessToken) {
        return { 'Authorization': "Bearer " + user.accessToken };
    } else {
        return {};
    }
}
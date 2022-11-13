import axios from 'axios';

const API_URL = '/api/categories/';

class CategoryService {
    /**
     * Get all post categories
     */
    async getCategories() {
        const response = await axios.get(API_URL);
        const data = await response.data;
        return data;
    }
}

export default new CategoryService();
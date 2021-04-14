import axios from 'axios';
import authHeader from './auth-header';

const test_URL = "api/test/";

class UserService{
    getPublicContent(){
        return axios.get(test_URL + 'all');
    }

    getUserBoard(){
        return axios.get(test_URL + 'user', {headers: authHeader()});
    }

    getPremiumBoard(){
        return axios.get(test_URL + 'premium', {headers: authHeader()});
    }

    getRootBoard(){
        return axios.get(test_URL + 'root', {headers: authHeader()});
    }

}

export default new UserService();
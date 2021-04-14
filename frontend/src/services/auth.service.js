import axios from 'axios'

const URL = "auth/"

class AuthService{
    async login(user){ // block execution to get log in 
        return await axios.post('auth/signin', {
            username: user.username,
            password: user.password,
        });
    }

    logout(){
        localStorage.removeItem('user');
    }

    register(user){
        return axios.post(URL + 'signup', {
            firstName: user.firstName,
            lastName: user.lastName,
            email: user.email,
            username: user.username,
            password : user.password,
            role : user.roles
        });
    }
}

export default new AuthService();
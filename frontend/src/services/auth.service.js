import axios from 'axios'

const URL = "auth/"

class AuthService{
    async login(user){
        axios.post('auth/signin', {
            username: user.username,
            password: user.password,
        })
        .then(response => {
            if(response.data.jwt){
                localStorage.setItem('user', JSON.stringify(response.data))
            }
            return response.data
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
import AuthService from '../../services/auth.service';

const user = JSON.parse(localStorage.getItem('user'));

//Initial state
const state = user
? { status: { loggedIn: true }, user}
: { status: { loggedIn: false}, user: null};




const actions = {
    login({commit}, user){
        console.log("user before calling server authservice: "+ user);
        return AuthService.login(user).then(
            response => {
                if(response.data.jwt)
                    localStorage.setItem('user', JSON.stringify(response.data));
                commit('loginSuccess', response.data);
                return Promise.resolve(response);
            },
            error => {
                commit('loginFailure');
                return Promise.reject(error);
            }
        );
    },

    logout({commit}){
        AuthService.logout();
        commit('logout');
    },

    register({commit}, user){
        return AuthService.register(user).then(
            response => {
                commit('registerSuccess');
                return Promise.resolve(response.data);
            },
            error => {
                commit('registerFailure');
                return Promise.reject(error);
            }
        );
    }
}

    
const mutations = {
    loginSuccess(state, user){
        state.status.loggedIn = true,
        console.log("user in mutation: " + user);
        state.user = user;
        
    },
    loginFailure(state){
        state.status.loggedIn = false;
        state.user = null;
    },
    logout(state){
        state.status.loggedIn = false;
        state.user = null;
    },
    registerSuccess(state){
        state.status.loggedIn = false;
    },
    registerFailure(state){
        state.status.loggedIn = false;
    }
}

const getters = {
    isLoggedIn: state => state.status.loggedIn,
    currentUser: state => state.user
}

    

export default{
    namespaced: true,
    state,
    actions,
    mutations,
    getters,
    

}
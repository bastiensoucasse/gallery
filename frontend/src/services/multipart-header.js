export default function authHeader(){
    let user = JSON.parse(localStorage.getItem("user"));

    if(user && user.jwt){
        return { Authorization: 'Bearer ' + user.jwt, 'Content-Type': 'multipart/form-data'};
    }else{
        return {'Content-Type': 'multipart/form-data'};
    }
}
import axios from 'axios';
import authHeader from './auth-header';

const public_URL = "images/";
const user_URL = "user/";



class ImageService{

    async getImageList(user){
        if(user){
            return await axios.get(user_URL + user.id + '/images', {headers: authHeader()});
        }
        return await axios.get(public_URL);
    }

    getData(user, image_id){
        if(user){
            return axios.get(user_URL + user.id + '/images/' + image_id, {responseType: "blob", headers: authHeader()});
        }else{
            return axios.get(public_URL + image_id, {responseType: "blob"});
        }
    }

    

    deleteImage(id, user){
        if(user != null)
            return axios.delete(public_URL + id + "/" + user.username + "/" + user.id, {headers: authHeader()});
    }


}

export default new ImageService();
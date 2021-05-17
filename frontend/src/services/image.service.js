import axios from 'axios';
import authHeader from './auth-header';
import multipartHeader from './multipart-header';

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
        return new Promise((resolve, reject) =>{
            reject("You must be authentified to delete Images");
        })
    }

    applyAlgorithm(parameters){
        return axios.get(public_URL + parameters, {responseType: "blob", headers: authHeader()});
    }

    applyAlgorithmOnFile(parameters, multipart_file){
        return axios.post(public_URL + parameters, multipart_file, {responseType: "blob", headers: multipartHeader()});
    }

    readBlob(response){
        let reader = new window.FileReader();
        reader.readAsDataURL(response.data);
        return reader;

    }

    save(user, formData){
        if(user != null)
            return axios.post(public_URL + user.id, formData, {headers: multipartHeader()});
        return new Promise((resolve, reject) =>{
            reject("You must be authentified to save Images");
        })
        
    }


}

export default new ImageService();
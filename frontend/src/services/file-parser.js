class FileParser{

    b64toBlob(b64Data, contentType, sliceSize) {
        contentType = contentType || '';
        sliceSize = sliceSize || 512;

        var byteCharacters = atob(b64Data);
        var byteArrays = [];

        for (var offset = 0; offset < byteCharacters.length; offset += sliceSize) {
            var slice = byteCharacters.slice(offset, offset + sliceSize);

            var byteNumbers = new Array(slice.length);
            for (var i = 0; i < slice.length; i++) {
                byteNumbers[i] = slice.charCodeAt(i);
            }

            var byteArray = new Uint8Array(byteNumbers);

            byteArrays.push(byteArray);
        }

      var blob = new Blob(byteArrays, {type: contentType});
      return blob;
    }

    parseURLDataAsFormFile(ImageURL, filename){
            
        // Split the base64 string in data and contentType
        let block = ImageURL.split(";");
        // Get the content type of the image
        let contentType = block[0].split(":")[1];// In this case "image/gif"
        // get the real base64 content of the file
        let realData = block[1].split(",")[1];// In this case "R0lGODlhPQBEAPeoAJosM...."

        // Convert it to a blob to upload
        let blob = this.b64toBlob(realData, contentType);

        // Create a FormData and append the file with "image" as parameter name
        let formDataToUpload = new FormData();
        formDataToUpload.append("file", blob, filename);

        return formDataToUpload;
    
    }


}

export default new FileParser();
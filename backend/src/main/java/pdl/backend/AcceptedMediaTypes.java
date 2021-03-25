package pdl.backend;

import java.util.Arrays;

import org.springframework.http.MediaType;

public enum AcceptedMediaTypes {

    //if this enum becomes too big, it's possible to add a Set<String> for faster checking method

    JPEG(MediaType.IMAGE_JPEG_VALUE),
    TIFF("image/tiff");

    private String type;

    
    AcceptedMediaTypes(String type){
        this.type = type;
    }


    public String getType(){
        return this.type;
    }

    public static boolean contains(String type){
        return Arrays.stream(AcceptedMediaTypes.values()).anyMatch((t) -> type.equals(t.type));
    }
}

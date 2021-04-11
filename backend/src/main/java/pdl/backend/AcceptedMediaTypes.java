package pdl.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import javax.print.attribute.standard.Media;

import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.http.MediaType;

public class AcceptedMediaTypes {

    private String type;
    private static Set<MediaType> types = Set.of(MediaType.IMAGE_JPEG, MediaType.valueOf("image/tiff"), MediaType.IMAGE_PNG);

    private AcceptedMediaTypes() {
    }

    public String getType() {
        return this.type;
    }

    public static boolean contains(String type){
        return types.contains(MediaType.valueOf(type));
    }

    public static boolean contains(MediaType type){
        return types.contains(type);
    }

    public static Set<MediaType> values(){
        return types;
    }

    public static Collection<String> toJson(){
        ArrayList<String> jsonStrings = new ArrayList<>();
        for (MediaType mediaType : types) {
            jsonStrings.add("{\"type\":\"" + mediaType.toString() +"\", \"subType\": \"" + mediaType.getSubtype() + "\"}");
        }
        return jsonStrings;
    }
    
}

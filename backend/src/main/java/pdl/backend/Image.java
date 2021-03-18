package pdl.backend;

import org.springframework.http.MediaType;

public class Image {
    private static Long count = Long.valueOf(0);
    private Long id;
    private String name;
    private MediaType type;
    private String size;
    private byte[] data;

    public Image(final String name, final byte[] data) {
        this.id = count++;
        this.name = name;
        this.data = data;
    }

    public Image(final String name, final byte[] data, MediaType type, String size){
        this.id = count++;
        this.name = name;
        this.data = data;
        this.type = MediaType.IMAGE_JPEG;
        this.size = size;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MediaType getType(){
        return type;
    }

    public String getSize(){
        return size;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public byte[] getData() {
        return data;
    }
}

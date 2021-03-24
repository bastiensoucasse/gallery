package pdl.backend;

import org.springframework.http.MediaType;

public class Image {
    private static Long count = Long.valueOf(0);
    private Long id;
    private String name;
    private MediaType type;
    private String size;
    private byte[] data;


    public Image(){
        super();
    }

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

    public Image(long id, String name, MediaType type, String size){
        this.id = id;
        this.name = name;
        this.type = type;
        this.size = size;
        this.data = null;
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

    @Override
    public String toString() {
        return "{\"id\":\"" + getId() + "\", \"name\": \"" + name + "\", \"type\": \"" + type.toString() + "\", \"size\": \"" + size + "\"" + "}"; 
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || obj.getClass() != this.getClass())
            return false;
        final Image other = (Image) obj;
        if(obj == this)
            return true;

        return other.name.equals(name) && other.data.equals(data) && other.id.equals(id) && other.size.equals(size) && other.type.equals(type) ;
        
    }
}

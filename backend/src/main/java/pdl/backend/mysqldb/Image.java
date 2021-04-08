package pdl.backend.mysqldb;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.springframework.http.MediaType;

@Entity
public class Image {
    private static int count = 0;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    @Column(name = "name", columnDefinition="VARCHAR(128)")
    private String name;
    @Column(name = "type", columnDefinition="VARCHAR(128)")
    private MediaType type;

    @Column(name = "size", columnDefinition="VARCHAR(128)")
    private String size;
    @Lob
    @Column(name = "data", columnDefinition="LONGBLOB")
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
        //this.id = count++;
        this.name = name;
        this.data = data;
        this.type = type;
        this.size = size;
    }



    public Integer getId() {
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

    /**
     * Return a json string representation of the metaData of the image
     */
    @Override
    public String toString() {
        return "{\"id\":\"" + getId() + "\", \"name\": \"" + name + "\", \"type\": \"" + type + "\", \"size\": \"" + size + "\"" + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || obj.getClass() != this.getClass())
            return false;
        final Image other = (Image) obj;
        if(obj == this)
            return true;

        return other.id == id && other.name.equals(name) && other.data.equals(data) && other.size.equals(size) && other.type.equals(type);

    }
}

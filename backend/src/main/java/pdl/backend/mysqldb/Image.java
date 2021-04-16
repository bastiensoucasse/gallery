package pdl.backend.mysqldb;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Entity
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "size")
    private String size;

    @Lob
    @Column(name = "data")
    private byte[] data;

    @Column(name = "new", columnDefinition = "TINYINT(1)")
    private boolean newI;

    @ManyToOne
    @JoinColumn(name = "FK_UserId")
    private User user;

    public Image() {
        super();
    }

    public Image(final String name, final byte[] data) {
        this.name = name;
        this.data = data;
        this.newI = true;
    }

    public Image(final String name, final byte[] data, final MediaType type, final String size) {
        this.name = name;
        this.data = data;
        this.type = type.toString();
        this.size = size;
        this.newI = true;
    }

    public Image(final String name, final byte[] data, final MediaType type, final long[] size) {
        this.name = name;
        this.data = data;
        this.type = type.toString();
        this.size = "";
        this.newI = true;
        for (long i : size)
            this.size += Long.toString(i) + "*";
        this.size = this.size.substring(0, this.size.length() - 1);
    }

    public Image(final String name, final byte[] data, final MediaType type, String size, User user, boolean isNew) {
        this.name = name;
        this.data = data;
        this.type = type.toString();

        this.newI = isNew;
        this.size = size;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MultiValueMap<String, String> getProperties(){
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("name", name);
        map.add("id", "" +id);
        map.add("type", type);
        map.add("size", size);
        System.out.println(map);
        return map;
    }

    public MediaType getType() {
        return MediaType.valueOf(type);
    }

    public String getSize() {
        return size;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public byte[] getData() {
        return data;
    }

    public User getUser()
    {
        return this.user;
    }

    public String getNameWithoutExtension() {
        return name.split("\\.")[0];
    }

    public boolean isNew() {
        return this.newI;
    }

    public void setIsNew(boolean n) {
        this.newI = n;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Return a json string representation of the metaData of the image
     */
    @Override
    public String toString() {
        return "{\"id\":\"" + getId() + "\", \"name\": \"" + name + "\", \"type\": \"" + type + "\", \"size\": \""
                + size + "\"," + "\"newI\": \"" + newI + "\"}";
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        final Image other = (Image) obj;
        if (obj == this)
            return true;

        return other.id == id && other.name.equals(name) && other.data.equals(data) && other.size.equals(size)
                && other.type.equals(type);
    }
}

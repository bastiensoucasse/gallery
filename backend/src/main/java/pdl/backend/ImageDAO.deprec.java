/*
package pdl.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

import pdl.backend.mysqldb.Image;

@Repository
public class ImageDAO implements DAO<Image> {
    private final Map<Long, Image> images = new HashMap<>();

    public ImageDAO() {
    }

    @Override
    public Optional<Image> retrieve(final long id) {
        final Image image = images.get(id);

        if (image == null)
            return Optional.empty();

        return Optional.of(image);
    }

    @Override
    public List<Image> retrieveAll() {
        final List<Image> imageList = new ArrayList<Image>();

        for (final Image i : images.values())
            imageList.add(i);

        return imageList;
    }

    @Override
    public void create(final Image image) {
        images.put(image.getId(), image);
    }

    @Override
    public void update(final Image image, final String[] args) {
        // Not used
    }

    @Override
    public void delete(final Image image) {
        for (final long id : images.keySet()) {
            if (images.get(id).equals(image)) {
                images.remove(id);
                return;
            }
        }
    }

    public long getId(String name) {
        long id = -1;
        for (Image i : images.values()) {
            if (i.getName().equals(name))
                id = i.getId();
        }
        return id;
    }
}
*/

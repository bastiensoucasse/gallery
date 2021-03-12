package pdl.backend;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

@Repository
public class ImageDAO implements DAO<Image> {
    private final Map<Long, Image> images = new HashMap<>();

    public ImageDAO() {
        final ClassPathResource imgFile = new ClassPathResource("osabat.jpg");
        byte[] fileContent;
        try {
            final File file = imgFile.getFile();
            //ImageIO.read(file).getWidth() 
            //ImageIO.read(file).getWidth()
            //ImageIO.read(file).getColorModel().getNumComponents() ); 
            //Files.probeContentType(file.toPath()));
            fileContent = Files.readAllBytes(file.toPath());
            final Image img = new Image("osabat.jpg", fileContent);
            images.put(img.getId(), img);
        } catch (final IOException e) {
            e.printStackTrace();
        }
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
}

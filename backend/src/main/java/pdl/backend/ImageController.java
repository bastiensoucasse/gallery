package pdl.backend;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
public class ImageController {
    /**
     * Image data access object (database).
     */
    private final ImageDAO imageDAO;

    /**
     * JSON mapper.
     */
    @Autowired
    private ObjectMapper mapper;

    /**
     * Creates an {@code ImageController} connected to an {@code ImageDAO}.
     * 
     * @param imageDAO the image data access object (database)
     */
    @Autowired
    public ImageController(final ImageDAO imageDAO) {
        this.imageDAO = imageDAO;
    }

    /**
     * Gets an image from the DAO and sends it as an HTTP response.
     * 
     * @param id the image identifier (number)
     * @return the HTTP response (status 200 with image if success, status 404 if no
     *         image was found)
     */
    @RequestMapping(value = "/images/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> getImage(@PathVariable("id") final long id) {
        final Image image = imageDAO.retrieve(id).orElse(null);

        if (image == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image.getData());
    }

    /**
     * Sends a 400 response when trying to delete an image with no identifier.
     * 
     * @return the HTTP response (status 400)
     */
    @RequestMapping(value = "/images", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteImage() {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Deletes an image from the DAO.
     * 
     * @param id the image identifier (number)
     * @return the HTTP response (status 200 if success, status 404 if no image was
     *         found)
     */
    @RequestMapping(value = "/images/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteImage(@PathVariable("id") final long id) {
        final Image image = imageDAO.retrieve(id).orElse(null);

        if (image == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        imageDAO.delete(image);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Add an image to the DAO.
     * 
     * @param file               the image (JPEG) to add
     * @param redirectAttributes
     * @return the HTTP response (status 201 if success, status 415 if the image
     *         isn't in the JPEG format, status 400 if the request is incorrect)
     */
    @RequestMapping(value = "/images", method = RequestMethod.POST)
    public ResponseEntity<?> addImage(@RequestParam("file") final MultipartFile file,
            final RedirectAttributes redirectAttributes) {
        if (!file.getContentType().equals(MediaType.IMAGE_JPEG_VALUE))
            return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);

        try {
            final Image image = new Image(file.getOriginalFilename(), file.getBytes());
            imageDAO.create(image);
        } catch (final IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Gets the image list from the DAO.
     * 
     * @return the raw JSON nodes
     */
    @RequestMapping(value = "/images", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public ArrayNode getImageList() {
        final ArrayNode nodes = mapper.createArrayNode();

        for (final Image image : imageDAO.retrieveAll())
            try {
                nodes.add(mapper
                        .readTree("{ \"id\": \"" + image.getId() + "\", \"name\": \"" + image.getName() + "\" }"));
            } catch (final JsonProcessingException e) {
                e.printStackTrace();
            }

        return nodes;
    }
}

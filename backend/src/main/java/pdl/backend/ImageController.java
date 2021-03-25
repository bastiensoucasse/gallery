package pdl.backend;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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
    @RequestMapping(value = "/images/{id}", method = RequestMethod.GET, produces = { MediaType.IMAGE_JPEG_VALUE,
            "image/tiff" })
    public ResponseEntity<?> getImage(@PathVariable("id") final long id) {
        final Image image = imageDAO.retrieve(id).orElse(null);

        if (image == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return ResponseEntity.ok().contentType(image.getType()).body(image.getData());
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

        if (!AcceptedMediaTypes.contains(file.getContentType()))
            return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        try {
            final Image image = new Image(file.getOriginalFilename(), file.getBytes(), Utils.typeOfFile(file),
                    Utils.sizeOfImage(file));
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
                nodes.add(mapper.readTree(image.toString()));
            } catch (final JsonProcessingException e) {
                e.printStackTrace();
            }
        return nodes;
    }

    @PostConstruct
    /**
     * Method launches when the server is starting
     */
    public void onStart() {
        String path = "/images/";
        try {
            Path path_of_resource = getPathOfResource(path);
            saveImagesFolder(path_of_resource);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Apply an algorithm to an Image
     *
     * @param id        id of the image
     * @param algorithm algorithm passed as parameters through URL
     * @return ResponseEntity: (200): if the image was processed (400): -if the
     *         algorithm doesn't exist -one of the paramter doesn't exist -the value
     *         of the parameter are invalid
     *
     *         (404): if no images exist with the given id (500): if the algorithm
     *         fail for internal reason
     */
    @RequestMapping(value = "/images/{id}", params = "algorithm", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public ResponseEntity<?> executeAlgorithm(@PathVariable("id") final long id,
            @RequestParam Map<String, String> algorithm) {
        final Image image = imageDAO.retrieve(id).orElse(null);
        if (image == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        String name = algorithm.get("algorithm"); // get the name of the algorithm
        algorithm.remove("algorithm"); // remove from the set

        try {
            Image proccessedImage = AlgorithmManager.Instance().applyAlgorithm(name, algorithm.values(), image);
            imageDAO.create(proccessedImage);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.badRequest().body("Algorithm doesn't exists");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.badRequest().body("Impossible to parse Parameters not a Number");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.badRequest().body("Invalid Arguments");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Upload all images (jpeg, tif) in the images folder on the server.
     *
     * @throws IOException
     */
    public void saveImagesFolder(Path path_of_resource) throws IOException {
        Path path = path_of_resource;
        Set<String> listImages = new HashSet<>();
        listImages = listFiles(path);
        System.out.println(listImages);
        listImages.forEach(i -> {
            try {
                byte[] fileContent = Files.readAllBytes(Paths.get(i));
                MediaType type = MediaType.parseMediaType(Files.probeContentType(Paths.get(i)));
                BufferedImage bufferedImage = ImageIO.read(Paths.get(i).toFile());
                String size = "" + bufferedImage.getWidth() + "*" + bufferedImage.getHeight() + "*"
                        + bufferedImage.getColorModel().getNumComponents();
                imageDAO.create(new Image(Paths.get(i).getFileName().toString(), fileContent, type, size));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     *
     * @param p the parent file you need to scan
     * @return a set of strings, indicating the full path of jpeg and tif images.
     * @throws IOException
     */
    public Set<String> listFiles(Path p) throws IOException {
        try (Stream<Path> stream = Files.walk(p)) {
            return stream.map(Path::toString)
                    .filter(file -> file.endsWith(".tif") || file.endsWith(".tiff") || file.endsWith(".jpeg")
                            || file.endsWith(".jpg") || file.endsWith(".jpe") || file.endsWith(".jfif"))
                    .collect(Collectors.toSet());
        }
    }

    public Path getPathOfResource(String p) throws IOException {
        final ClassPathResource resource = new ClassPathResource(p);
        File f = resource.getFile();
        return Paths.get(f.getAbsolutePath());
    }
}

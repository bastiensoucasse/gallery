package pdl.backend.controller;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pdl.backend.AcceptedMediaTypes;
import pdl.backend.AlgorithmManager;
import pdl.backend.Utils;
import pdl.backend.mysqldb.Image;
import pdl.backend.mysqldb.ImageRepository;
import pdl.backend.mysqldb.UserRepository;
import pdl.processing.ImageManager;

@RestController
public class ImageController {
    /**
     * Image data access object (database).
     */
    // private final ImageDAO imageDAO;

    @Autowired
    private ImageRepository imageRepository;


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
    // public ImageController(ImageDAO imageDAO) {
    // this.imageDAO = imageDAO;
    // }

    /**
     * Gets an image from the DAO and sends it as an HTTP response.
     *
     * @param id the image identifier (number)
     * @return the HTTP response (status 200 with image if success, status 404 if no
     *         image was found)
     */
    @RequestMapping(path = "/images/{imageID}", produces = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE,
            "image/tiff" })
    public ResponseEntity<?> getImage(@PathVariable("imageID") final int imageID) {
        // Image
        final Image image = imageRepository.findById(imageID).orElse(null);

        // Not found if no image
        if (image == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        // Forbidden if private
        if (image.getUser() != null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        // Otherwise
        if (image.getType().equals(MediaType.valueOf("image/tiff"))) { // Check if the type is tiff
            try { // Try to convert the bytes to jpeg
                byte[] jpegContent = ImageManager.convertImageBytes(image.getData(), MediaType.IMAGE_PNG);
                return ResponseEntity.ok().contentType(image.getType()).body(jpegContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
    public ResponseEntity<?> deleteImage(@PathVariable("id") final int id) {
        if (imageRepository.findById(id).isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        imageRepository.deleteById(id);
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
        Image image;
        if (!AcceptedMediaTypes.contains(file.getContentType()))
            return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        try {
            image = new Image(file.getOriginalFilename(), file.getBytes(), Utils.typeOfFile(file),
                    Utils.sizeOfImage(file));
            imageRepository.save(image);
        } catch (final IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            return ResponseEntity.created(new URI("/" + image.getId())).body("" + image.getId());
        } catch (final URISyntaxException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Gets the image list from the database.
     *
     * @return the raw JSON nodes
     */
    @GetMapping(path = "/images", produces = "application/json; charset=UTF-8")
    public @ResponseBody ArrayNode getImageList() {
        final ArrayNode nodes = mapper.createArrayNode();
        for (final Image image : imageRepository.findAllPublic())
            try {
                nodes.add(mapper.readTree(image.toString()));
            } catch (final JsonProcessingException e) {
                e.printStackTrace();
            }
        return nodes;
    }

    /**
     * Method launches when the server is starting
     */
    @PostConstruct
    public void onStart() {
        final String path = "/images/";
        try {
            final Path path_of_resource = getPathOfResource(path);
            saveImagesFolder(path_of_resource);
        } catch (final IOException e1) {
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
    public ResponseEntity<?> executeAlgorithm(@PathVariable("id") final int id,
            @RequestParam final Map<String, String> algorithm) {
        final Image image = imageRepository.findById(id).orElse(null);
        if (image == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        final String name = algorithm.get("algorithm"); // get the name of the algorithm
        algorithm.remove("algorithm"); // remove from the set

        Image proccessedImage;
        try {
            proccessedImage = AlgorithmManager.Instance().applyAlgorithm(name, algorithm.values(), image);
            imageRepository.save(proccessedImage);
        } catch (final NoSuchMethodException e) {
            new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.badRequest().body("Algorithm doesn't exists");
        } catch (final NumberFormatException e) {
            new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.badRequest().body("Impossible to parse Parameters not a Number");
        } catch (final IllegalArgumentException e) {
            new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.badRequest().body("Invalid Arguments");
        } catch (final Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML)
                .body("<script>location.href = '/" + proccessedImage.getId() + "';</script>");
    }

    @RequestMapping(value = "/images/{id}", params = "format", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> convertImageFormat(@PathVariable("id") final int id, @RequestParam final String format) {
        if (!AcceptedMediaTypes.contains(format))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("format not supported by the server");

        Optional<Image> input = imageRepository.findById(id);
        if (input.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        try {
            Image output = ImageManager.convertImage(input.get(), MediaType.valueOf(format));
            return ResponseEntity.ok().header("name", output.getName()).contentType(output.getType())
                    .body(output.getData());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok().header("name", input.get().getName()).contentType(input.get().getType())
                    .body(input.get().getData());
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/images/{id}/saving")
    @ResponseBody
    public ResponseEntity<?> saveImage(@PathVariable("id") final int id) {
        Image i = imageRepository.findById(id).orElse(null);
        if (i.equals(null))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        i.setIsNew(false);
        imageRepository.save(i);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/images/{id}/properties")
    @ResponseBody
    public JsonNode getImageProperties(@PathVariable("id") final int id) {
        Image i = imageRepository.findById(id).orElse(null);
        if (i.equals(null))
            return null;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.valueToTree(i);
        // ((ObjectNode) node).put("new", i.isNew());
        return node;
    }

    @GetMapping(path = "/images/supportedMedia", produces = "application/json; charset=UTF-8")
    public @ResponseBody ArrayNode listOfSupportedMedia() {
        final ArrayNode nodes = mapper.createArrayNode();

        for (String jsonString : AcceptedMediaTypes.toJson()) {
            try {
                nodes.add(mapper.readTree(jsonString));
            } catch (final JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return nodes;

    }

    /**
     * Upload all images (jpeg, tif) in the images folder on the server.
     *
     * @throws IOException
     */
    public void saveImagesFolder(final Path path_of_resource) throws IOException {
        final Path path = path_of_resource;
        Set<String> listImages = new HashSet<>();
        listImages = listFiles(path);
        List<String> found = new ArrayList<>();
        Iterator<Image> image_it = imageRepository.findAll().iterator();
        // Filter images that have the same name in the db and the images in the
        // resource folder.
        while (image_it.hasNext()) {
            Image next_i = image_it.next();
            Iterator<String> list_it = listImages.iterator();
            while (list_it.hasNext()) {
                String next_s = list_it.next();
                if (next_i.getName().equals(Paths.get(next_s).getFileName().toString()))
                    found.add(next_s);
            }
        }
        listImages.removeAll(found);
        System.out.println(listImages);
        listImages.forEach(i -> {
            try {
                final byte[] fileContent = Files.readAllBytes(Paths.get(i));
                Image image = new Image(Paths.get(i).getFileName().toString(), fileContent,
                        Utils.typeOfFile(Paths.get(i)), Utils.sizeOfImage(Paths.get(i)));
                image.setIsNew(false);
                imageRepository.save(image);

            } catch (final IOException e) {
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
    public Set<String> listFiles(final Path p) throws IOException {
        try (Stream<Path> stream = Files.walk(p)) {
            return stream.map(Path::toString)
                    .filter(file -> file.endsWith(".tif") || file.endsWith(".tiff") || file.endsWith(".jpeg")
                            || file.endsWith(".jpg") || file.endsWith(".jpe") || file.endsWith(".jfif"))
                    .collect(Collectors.toSet());
        }
    }

    public Path getPathOfResource(final String p) throws IOException {
        final ClassPathResource resource = new ClassPathResource(p);
        File f;
        try {
            f = resource.getFile();
        } catch (final IOException e) {
            throw new IOException("The image folder does not exist", e.getCause());
        }
        return Paths.get(f.getAbsolutePath());
    }
}

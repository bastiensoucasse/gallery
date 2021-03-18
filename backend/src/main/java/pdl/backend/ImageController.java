package pdl.backend;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

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
            final Image image = new Image(file.getOriginalFilename(), file.getBytes(), Utils.typeOfFile(file), Utils.sizeOfImage(file));
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
                nodes.add(mapper.readTree("{ \"id\": \"" + image.getId() + "\", \"name\": \"" + image.getName()
                        + "\", \"type\": \"" + image.getType() + "\", \"size\": \"" + image.getSize() + "\"" + "}"));
            } catch (final JsonProcessingException e) {
                e.printStackTrace();
            }

        return nodes;
    }


    /**
     * Apply an algorithm to an Image
     * @param id id of the image
     * @param algorithm algorithm passed as parameters through URL
     * @return ResponseEntity:  (200): if the image was processed
     *                          (400):  -if the algorithm doesn't exist
     *                                  -one of the paramter doesn't exist
     *                                  -the value of the parameter are invalid
     * 
     *                          (404): if no images exist with the given id
     *                          (500): if the algorithm fail for internal reason
     */
    @RequestMapping(value = "/images/{id}", params = "algorithm", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public ResponseEntity<?> executeAlgorithm(@PathVariable("id") final long id, @RequestParam Map<String, String> algorithm){
        final Image image = imageDAO.retrieve(id).orElse(null);
        if (image == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        
        String method = algorithm.get("algorithm"); // get the name of the algorithm
        algorithm.remove("algorithm"); // remove from the set

        System.out.println(method); // debug
        System.out.println(algorithm.entrySet()); //debug

        ArrayList<Integer> args = Utils.parseListOfArguments(algorithm.values());
        System.out.println(args);
        
        //TO DO
        

        return new ResponseEntity<>(HttpStatus.OK);
    }



    /**
     * Upload all images in the images folder on the server.
     * 
     * @throws IOException
     */
    @PostConstruct
    public void saveImagesFolder() throws IOException {
        // Path path = Paths.get(System.getProperty("user.dir"), "/images");
        final ClassPathResource resource = new ClassPathResource("/images/");
        File f = resource.getFile();
        Path path = Paths.get(f.getAbsolutePath());
        if (Files.notExists(path, LinkOption.NOFOLLOW_LINKS)) {
            try {
                throw new Exception("Folder Images does not exist\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Set<String> listImages = new HashSet<>();
        try {
            listImages = Utils.listFiles(path);
            System.out.println(listImages);

        } catch (IOException e) {
            e.printStackTrace();
        }
        listImages.forEach(i -> {
            try {
                String path_images = ("/images/").concat(Paths.get(i).getFileName().toString());
                final ClassPathResource imgFile = new ClassPathResource(path_images);
                final File file = imgFile.getFile();
                byte[] fileContent = Files.readAllBytes(file.toPath());
                imageDAO.create(new Image(Paths.get(i).getFileName().toString(), fileContent, Utils.typeOfFile(file), Utils.sizeOfImage(file)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    
}

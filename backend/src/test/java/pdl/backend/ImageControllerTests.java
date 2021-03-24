package pdl.backend;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class ImageControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ImageDAO imageDAO;

    @BeforeAll
    public static void reset() {
        ReflectionTestUtils.setField(Image.class, "count", Long.valueOf(0));
    }

    @Test
    @Order(1)
    public void getImageListShouldReturnSuccess() throws Exception {
        this.mockMvc.perform(get("/images")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @Order(2)
    public void getImageShouldReturnNotFound() throws Exception {
        this.mockMvc.perform(get("/images/200")).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    @Order(3)
    public void getImageShouldReturnSuccess() throws Exception {
        this.mockMvc.perform(get("/images/0")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @Order(4)
    public void deleteImageShouldReturnBadRequest() throws Exception {
        this.mockMvc.perform(delete("/images")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @Order(5)
    public void deleteImageShouldReturnNotFound() throws Exception {
        long lastId = (long) ReflectionTestUtils.getField(Image.class, "count");
        this.mockMvc.perform(delete("/images/" + (++lastId))).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    @Order(6)
    public void deleteImageShouldReturnSuccess() throws Exception {
        this.mockMvc.perform(delete("/images/0")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @Order(7)
    public void createImageShouldReturnSuccess() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "osabat.jpeg", MediaType.IMAGE_JPEG_VALUE,
                new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/images/osabat.jpeg"));
        this.mockMvc.perform(multipart("/images").file(file)).andDo(print()).andExpect(status().isCreated());
    }

    @Test
    @Order(8)
    public void createImageShouldReturnUnsupportedMediaType() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "osabat.png", MediaType.IMAGE_PNG_VALUE,
                new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/images/osabat.png"));
        this.mockMvc.perform(multipart("/images").file(file)).andDo(print())
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @Order(9)
    public void testGetPathOfResource() throws IOException {
        ImageDAO i = new ImageDAO();
        ImageController c = new ImageController(i);
        String path_1 = "/images/";
        String path_2 = "/image_test/another_one/jam.jpg";
        String path_3 = "/image_test/osabat.jpg";
        String path_4 = "/image_test/another_one/testing.txt";
        assertTrue(Files.exists(c.getPathOfResource(path_1)));
        assertTrue(Files.exists(c.getPathOfResource(path_2)));
        assertTrue(Files.exists(c.getPathOfResource(path_3)));
        assertTrue(Files.exists(c.getPathOfResource(path_4)));
    }

    @Test
    @Order(10)
    public void listFilesShouldReturnSucess() throws Exception {
        ImageDAO i = new ImageDAO();
        ImageController c = new ImageController(i);
        Path path_image_1 = c.getPathOfResource("/image_test/osabat.jpg");
        Path path_image_2 = c.getPathOfResource("/image_test/another_one/cyber.jpeg");
        Path path_image_3 = c.getPathOfResource("/image_test/another_one/jam.jpg");

        final ClassPathResource resource = new ClassPathResource("/image_test/");
        File f = resource.getFile();
        Path path_of_images = Paths.get(f.getAbsolutePath());
        Set<String> images = c.listFiles(path_of_images);

        Path path = Paths.get(System.getProperty("user.dir"), "/src/main/resources/images");
        Set<String> image_set = Utils.listFiles(path);
        assertTrue(image_set.size() != 0);
        images.forEach(ima -> assertTrue(ima.contains(".tif") || ima.contains(".jpeg")));
        assertFalse(images.contains(path_image_1.toString()));
        assertTrue(images.contains(path_image_2.toString()));
        assertFalse(images.contains(path_image_3.toString()));
    }

    @Test
    @Order(11)
    public void testSaveImagesFolder() throws IOException {
        ImageDAO i = new ImageDAO();
        String image_1 = "autumn.tif";
        String image_2 = "cyber.jpeg";
        String text = "testing.txt";
        ImageController c = new ImageController(i);
        int size = i.retrieveAll().size();
        Path p = c.getPathOfResource("/image_test/");
        c.saveImagesFolder(p);
        assertTrue(size < i.retrieveAll().size());
        assertTrue(i.getId(image_1) > 0);
        assertTrue(i.getId(image_2) > 0);
        assertFalse(i.getId(text) > 0);

        Image i1 = i.retrieve(i.getId(image_1)).orElse(null);
        assertFalse(i1.getSize().equals("null"));
        assertFalse(i1.getData() == null);
        System.out.println(i1.getType());
        assertTrue(i1.getType().equals(MediaType.IMAGE_JPEG) || i1.getType().equals(MediaType.valueOf("image/tiff")));

        Image i2 = i.retrieve(i.getId(image_2)).orElse(null);
        assertFalse(i2.getSize().equals("null"));
        assertFalse(i2.getData() == null);
        assertTrue(i2.getType().equals(MediaType.IMAGE_JPEG) || i1.getType().equals(MediaType.valueOf("image/tiff")));
    }

    @Test
    @Order(12)
    public void testGetMetaData() throws Exception {
        List<Image> listImages = imageDAO.retrieveAll();
        String jsonContent = "[";
        jsonContent += listImages.get(0).toString();
        listImages.remove(0);
        for (Image image : listImages) {
            jsonContent += "," + image.toString();
        }
        jsonContent += "]";

        mockMvc.perform(get("/images")).andExpect(content().contentType("application/json; charset=UTF-8"))
                .andExpect(content().json(jsonContent));
    }

    @Test
    @Order(13)
    public void toGrayScaleShouldReturnSuccess() throws Exception {
        mockMvc.perform(get("/images/1?algorithm=toGrayscale")).andExpect(status().isOk());
    }

    @Test
    @Order(14)
    public void changeBrightnessShouldReturnSucess() throws Exception {
        String args = "&gain=" + Utils.getRandomNumber(int.class, 0, 255);
        mockMvc.perform(get("/images/1?algorithm=changeBrightness" + args)).andExpect(status().isOk());
    }

    @Test
    @Order(15)
    public void colorizeShouldReturnSuccess() throws Exception {
        String args = "&hue=" + Utils.getRandomNumber(int.class, 0, 360);
        mockMvc.perform(get("/images/1?algorithm=colorize" + args)).andExpect(status().isOk());
    }

    @Test
    @Order(16)
    public void extendDynamicsShouldReturnSuccess() throws Exception {
        mockMvc.perform(get("/images/1?algorithm=extendDynamics")).andExpect(status().isOk());
    }

    @Test
    @Order(17)
    public void equalizeHistogramShouldReturnSuccess() throws Exception {
        String args = "&channel=" + Utils.getRandomNumber(int.class, 1, 2);
        mockMvc.perform(get("/images/1?algorithm=equalizeHistogram" + args)).andExpect(status().isOk());
    }

    @Test
    @Order(18)
    public void meanFilterShouldReturnSuccess() throws Exception {
        String args = "&radius=" + Utils.getRandomNumber(int.class, 0, 7);
        mockMvc.perform(get("/images/1?algorithm=meanFilter" + args)).andExpect(status().isOk());
    }

    @Test
    @Order(19)
    public void gaussianFilterShouldReturnSuccces() throws Exception {
        String args = "&radius=" + Utils.getRandomNumber(int.class, 0, 7);
        mockMvc.perform(get("/images/1?algorithm=gaussianFilter" + args)).andExpect(status().isOk());

    }

    @Test
    @Order(20)
    public void sobelOperatorShouldReturnSuccess() throws Exception {
        mockMvc.perform(get("/images/1?algorithm=sobelOperator")).andExpect(status().isOk());
    }

    @Test
    @Order(21)
    public void executeAlgorithmShouldReturnBadRequest() throws Exception {
        Set<String> listOfAlgorithms = AlgorithmManager.Instance().listAlgorithms();
        for (String name : listOfAlgorithms) {
            mockMvc.perform(get("/images/1?algorithm=" + name + "&x=1&y=2&z=5&delta=50"))
                    .andExpect(status().isBadRequest());
            mockMvc.perform(get("/images/1?algorithm=" + name.toUpperCase())).andExpect(status().isBadRequest());
            Set<Class<?>[]> parameterTypes = AlgorithmManager.Instance().listOfParameterType(name);
            for (Class<?>[] types : parameterTypes) {
                if (types.length > 2) {
                    mockMvc.perform(get("/images/1?algorithm=" + name + "&x=Hello World !"))
                            .andExpect(status().isBadRequest());
                }
            }
        }
    }

    @Test
    @Order(22)
    public void executeAlgorithmShouldReturnNotFound() throws Exception {
        long lastId = (long) ReflectionTestUtils.getField(Image.class, "count");

        Set<String> listOfAlgorithms = AlgorithmManager.Instance().listAlgorithms();
        for (String name : listOfAlgorithms) {
            mockMvc.perform(get("/images/" + (++lastId) + "?algorithm=" + name)).andExpect(status().isNotFound());
        }

    }

    @Test
    @Disabled
    public void TestPerformanceAlgorithmExecution() throws Exception {
        String parameters = "toGrayscale";
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            mockMvc.perform(get("/images/0?algorithm=" + parameters));
        }
        System.out.println(System.currentTimeMillis() - start);
    }
}

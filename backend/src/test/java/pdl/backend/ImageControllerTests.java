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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

import pdl.backend.controller.ImageController;
import pdl.backend.mysqldb.Image;
import pdl.backend.mysqldb.ImageRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class ImageControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImageController imageController;

    @Test
    @Order(1)
    public void getImageListShouldReturnSuccess() throws Exception {
        this.mockMvc.perform(get("/images")).andExpect(status().isOk());
    }

    @Test
    @Order(2)
    public void getImageShouldReturnNotFound() throws Exception {
        this.mockMvc.perform(get("/images/200")).andExpect(status().isNotFound());
    }

    @Test
    @Order(3)
    public void getImageShouldReturnSuccess() throws Exception {
        this.mockMvc.perform(get("/images/1")).andExpect(status().isOk());
    }

    @Test
    @Order(4)
    public void deleteImageShouldReturnBadRequest() throws Exception {
        this.mockMvc.perform(delete("/images")).andExpect(status().isBadRequest());
    }

    // @Test
    // @Order(5)
    // public void deleteImageShouldReturnNotFound() throws Exception {
    //     Integer lastId = (Integer) ReflectionTestUtils.getField(Image.class, "count");
    //     this.mockMvc.perform(delete("/images/" + (++lastId))).andExpect(status().isNotFound()).andDo(print());
    // }

    @Test
    @Order(6)
    public void deleteImageShouldReturnSuccess() throws Exception {
        this.mockMvc.perform(delete("/images/1")).andExpect(status().isOk());
    }

    // @Test
    // @Order(7)
    // public void createImageShouldReturnSuccess() throws Exception {
    //     final MockMultipartFile file = new MockMultipartFile("file", "osabat.jpg", MediaType.IMAGE_JPEG_VALUE,
    //             new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/images/osabat.jpeg"));
    //     this.mockMvc.perform(multipart("/images").file(file)).andExpect(status().isCreated());
    // }

    // @Test
    // @Order(8)
    // public void createImageShouldReturnUnsupportedMediaType() throws Exception {
    //     final MockMultipartFile file = new MockMultipartFile("file", "osabat.png", MediaType.IMAGE_PNG_VALUE,
    //             new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/images/osabat.png"));
    //     this.mockMvc.perform(multipart("/images").file(file)).andExpect(status().isUnsupportedMediaType());
    // }

    @Test
    @Order(9)
    public void testGetPathOfResource() throws IOException {
        ImageController c = new ImageController();
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
        ImageController c = new ImageController();
        Path path_image_1 = c.getPathOfResource("/image_test/osabat.jpg");
        Path path_image_2 = c.getPathOfResource("/image_test/another_one/cyber.jpeg");
        Path path_image_3 = c.getPathOfResource("/image_test/another_one/jam.jpg");
        Path path_image_4 = c.getPathOfResource("/image_test/another_one/sacre_coeur.jpg");
        Path path_image_5 = c.getPathOfResource("/image_test/insanity.jfif");
        Path path_image_6 = c.getPathOfResource("/image_test/osabat.png");

        final ClassPathResource resource = new ClassPathResource("/image_test/");
        File f = resource.getFile();
        Path path_of_images = Paths.get(f.getAbsolutePath());
        Set<String> images = c.listFiles(path_of_images);

        Path path = Paths.get(System.getProperty("user.dir"), "/src/main/resources/images");
        Set<String> image_set = Utils.listFiles(path);
        assertTrue(image_set.size() != 0);
        images.forEach(ima -> assertTrue(ima.contains(".tif") || ima.contains(".tif") || ima.contains(".jpeg")
                || ima.contains(".jpg") || ima.contains(".jpe") || ima.contains(".jfif")));
        assertTrue(images.contains(path_image_1.toString()));
        assertTrue(images.contains(path_image_2.toString()));
        assertTrue(images.contains(path_image_4.toString()));
        assertTrue(images.contains(path_image_5.toString()));
        assertTrue(images.contains(path_image_3.toString()));
        assertFalse(images.contains(path_image_6.toString()));
    }

    // @Test
    // @Order(11)
    // public void testSaveImagesFolder() throws IOException {
    //     String image_1 = "autumn.tif";
    //     String image_2 = "cyber.jpeg";
    //     String text = "testing.txt";
    //     String image_3 = "jam.jpg";
    //     String image_4 = "sacre_coeur.jpg";
    //     String image_5 = "insanity.jfif";

    //     ImageController c = new ImageController();
    //     Path p = c.getPathOfResource("/image_test/");
    //     imageController.saveImagesFolder(p);
    //     List<Image> l1 = imageRepository.findByName(image_1);
    //     List<Image> l2 = imageRepository.findByName(image_2);
    //     List<Image> l3 = imageRepository.findByName(text);
    //     List<Image> l4 = imageRepository.findByName(image_3);
    //     List<Image> l5 = imageRepository.findByName(image_4);
    //     List<Image> l6 = imageRepository.findByName(image_5);
    //     assertTrue(l1.size() == 1);
    //     assertTrue(l2.size() == 1);
    //     assertTrue(l3.size() == 0);
    //     assertTrue(l4.size() == 1);
    //     assertTrue(l5.size() == 1);
    //     assertTrue(l6.size() == 1);

    //     assertTrue(l1.get(0).getName().equals(image_1));
    //     assertTrue(l2.get(0).getName().equals(image_2));
    //     assertTrue(l4.get(0).getName().equals(image_3));
    //     assertTrue(l5.get(0).getName().equals(image_4));
    //     assertTrue(l6.get(0).getName().equals(image_5));

    //     Image i1 = l1.get(0);
    //     assertFalse(i1.getSize().equals(null));
    //     assertFalse(i1.getData().equals(null));
    //     System.out.println(i1.getType());
    //     assertTrue(i1.getType().equals(MediaType.valueOf("image/tiff")));

    //     Image i2 = l2.get(0);
    //     assertFalse(i2.getSize().equals(null));
    //     assertFalse(i2.getData().equals(null));
    //     assertTrue(i2.getType().equals(MediaType.IMAGE_JPEG));

    //     Image i3 = l4.get(0);
    //     assertFalse(i3.getSize().equals(null));
    //     assertFalse(i3.getData().equals(null));
    //     assertTrue(i3.getType().equals(MediaType.IMAGE_JPEG));

    //     Image i4 = l5.get(0);
    //     assertFalse(i4.getSize().equals(null));
    //     assertFalse(i4.getData().equals(null));
    //     assertTrue(i4.getType().equals(MediaType.IMAGE_JPEG));

    //     Image i5 = l6.get(0);
    //     assertFalse(i5.getSize().equals(null));
    //     assertFalse(i5.getData().equals(null));
    //     assertTrue(i5.getType().equals(MediaType.IMAGE_JPEG));
    // }

    // @Test
    // @Order(12)
    // public void testGetMetaData() throws Exception {
    //     final Iterable<Image> listImages = imageRepository.findAll();
    //     final Iterator<Image> it = listImages.iterator();
    //     String jsonContent = "[";
    //     jsonContent += it.next().toString();
    //     while (it.hasNext()) {
    //         jsonContent += "," + it.next().toString();
    //     }
    //     jsonContent += "]";

    //     mockMvc.perform(get("/images")).andExpect(content().contentType("application/json; charset=UTF-8"))
    //             .andExpect(content().json(jsonContent));
    // }

    // @Test
    // @Order(13)
    // public void toGrayScaleShouldReturnSuccess() throws Exception {
    //     mockMvc.perform(get("/images/1?algorithm=toGrayscale")).andExpect(status().isOk());
    // }

    // @Test
    // @Order(14)
    // public void changeBrightnessShouldReturnSucess() throws Exception {
    //     final String args = "&gain=" + Utils.getRandomNumber(int.class, 0, 255);
    //     mockMvc.perform(get("/images/1?algorithm=changeBrightness" + args)).andExpect(status().isOk());
    // }

    // @Test
    // @Order(15)
    // public void colorizeShouldReturnSuccess() throws Exception {
    //     final String args = "&hue=" + Utils.getRandomNumber(int.class, 0, 360);
    //     mockMvc.perform(get("/images/1?algorithm=colorize" + args)).andExpect(status().isOk());
    // }

    // @Test
    // @Order(16)
    // public void extendDynamicsShouldReturnSuccess() throws Exception {
    //     mockMvc.perform(get("/images/1?algorithm=extendDynamics")).andExpect(status().isOk());
    // }

    // @Test
    // @Order(17)
    // public void equalizeHistogramShouldReturnSuccess() throws Exception {
    //     final String args = "&channel=" + Utils.getRandomNumber(int.class, 1, 2);
    //     mockMvc.perform(get("/images/1?algorithm=equalizeHistogram" + args)).andExpect(status().isOk());
    // }

    // @Test
    // @Order(18)
    // public void meanFilterShouldReturnSuccess() throws Exception {
    //     final String args = "&radius=" + Utils.getRandomNumber(int.class, 0, 7);
    //     mockMvc.perform(get("/images/1?algorithm=meanFilter" + args)).andExpect(status().isOk());
    // }

    // @Test
    // @Order(19)
    // public void gaussianFilterShouldReturnSuccces() throws Exception {
    //     final String args = "&radius=" + Utils.getRandomNumber(int.class, 0, 7);
    //     mockMvc.perform(get("/images/1?algorithm=gaussianFilter" + args)).andExpect(status().isOk());

    // }

    // @Test
    // @Order(20)
    // public void sobelOperatorShouldReturnSuccess() throws Exception {
    //     mockMvc.perform(get("/images/1?algorithm=sobelOperator")).andExpect(status().isOk());
    // }

    @Test
    @Order(21)
    public void executeAlgorithmShouldReturnBadRequest() throws Exception {
        final Set<String> listOfAlgorithms = AlgorithmManager.Instance().listAlgorithms();
        for (final String name : listOfAlgorithms) {
            mockMvc.perform(get("/images/1?algorithm=" + name + "&x=1&y=2&z=5&delta=50"))
                    .andExpect(status().isBadRequest());
            mockMvc.perform(get("/images/1?algorithm=" + name.toUpperCase())).andExpect(status().isBadRequest());
            final Set<Class<?>[]> parameterTypes = AlgorithmManager.Instance().listOfParameterType(name);
            for (final Class<?>[] types : parameterTypes) {
                if (types.length > 2) {
                    mockMvc.perform(get("/images/1?algorithm=" + name + "&x=Hello World !"))
                            .andExpect(status().isBadRequest());
                }
            }
        }
    }

    // @Test
    // @Order(22)
    // public void executeAlgorithmShouldReturnNotFound() throws Exception {
    //     Integer lastId = (Integer) ReflectionTestUtils.getField(Image.class, "count");

    //     final Set<String> listOfAlgorithms = AlgorithmManager.Instance().listAlgorithms();
    //     for (final String name : listOfAlgorithms) {
    //         mockMvc.perform(get("/images/" + (++lastId) + "?algorithm=" + name)).andExpect(status().isNotFound());
    //     }

    // }

    // @Test
    // @Disabled
    // public void TestPerformanceAlgorithmExecution() throws Exception {
    //     final String parameters = "toGrayscale";
    //     final long start = System.currentTimeMillis();
    //     for (int i = 0; i < 100; i++) {
    //         mockMvc.perform(get("/images/0?algorithm=" + parameters));
    //     }
    //     System.out.println(System.currentTimeMillis() - start);
    // }
}

package pdl.backend;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.mockito.Mock;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
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
import org.springframework.test.web.servlet.MvcResult;

import ch.qos.logback.classic.pattern.Util;
import io.scif.SCIFIO;
import io.scif.img.SCIFIOImgPlus;
import net.imglib2.img.Img;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class ImageControllerTests {
    @Autowired
    private MockMvc mockMvc;

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
        this.mockMvc.perform(delete("/images/200")).andDo(print()).andExpect(status().isNotFound());
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
        assertTrue(i1.getType() == MediaType.IMAGE_JPEG || i1.getType() == MediaType.valueOf("Image/tif"));

        Image i2 = i.retrieve(i.getId(image_2)).orElse(null);
        assertFalse(i2.getSize().equals("null"));
        assertFalse(i2.getData() == null);
        assertTrue(i2.getType() == MediaType.IMAGE_JPEG || i1.getType() == MediaType.valueOf("Image/tif"));
    }

    @Test
    @Order(12)
    public void testGetMetaData() throws Exception {
        //!! TO DO
        ObjectMapper objectMapper = new ObjectMapper();
    
        Path path = Paths.get(System.getProperty("user.dir"), "/src/main/resources/images"); // directory of images
        Set<String> resourcesImages = Utils.listFiles(path); // list all the filename
        Map<String, File> expectedImages = new HashMap<>(); // map of all the expected file

        //go through all the files (jpeg) (tif) in the path
        for (String fileName : resourcesImages) {
            final ClassPathResource resource = new ClassPathResource("/images/" +fileName);
            expectedImages.put(resource.getFilename(), resource.getFile());
            System.out.println(resource.getFilename());
        }


        MvcResult result = mockMvc.perform(get("/images"))
        .andExpect(content().contentType("application/json; charset=UTF-8")).andReturn();
        String jsonData = result.getResponse().getContentAsString();
        System.out.println(jsonData);
        List<Image> listImages = objectMapper.readValue(jsonData, new TypeReference<List<Image>>(){});
        //assertTrue(listImages.size() == expectedImages.size());


        int id = 0;
        for (Image image : listImages) {
            String imageName = image.getName();
            assertTrue(image.getId() >= id);
            System.out.println(expectedImages.containsKey(imageName));
            assertTrue(expectedImages.containsKey(imageName));
            //assertTrue(MediaType.parseMediaType(image.getType()) quals(MediaType.IMAGE_JPEG));
            assertTrue(image.getSize().equals(Utils.sizeOfImage(expectedImages.get(imageName))));
            id++;
            
        }                              
    }


    @Test
    @Order(13)
    public void changeBrightnessShouldReturnSucess() throws Exception{
        String args = "&delta=" + Utils.getRandomNumber(int.class, 0, 255);
        mockMvc.perform(get("/images/1?algorithm=changeBrightness"+ args)).andExpect(status().isOk());
    }

    @Test
    @Order(14)
    public void toGrayScaleShouldReturnSuccess()throws Exception{
        mockMvc.perform(get("/images/1?algorithm=toGrayscale")).andExpect(status().isOk());
    }

    @Test
    @Order(15)
    public void colorizeShouldReturnSuccess() throws Exception{
        String args = "&delta=" + Utils.getRandomNumber(int.class, 0, 360);
        mockMvc.perform(get("/images/1?algorithm=colorize"+ args)).andExpect(status().isOk());
    }

    @Test
    @Order(16)
    public void extendDynamicsNoArgsShouldReturnSuccess() throws Exception{
        mockMvc.perform(get("/images/1?algorithm=extendDynamics")).andExpect(status().isOk());
    }

    @Test
    @Order(17)
    public void extendDynamicsShouldReturnSuccess() throws Exception{
        String args = "&min=" + Utils.getRandomNumber(int.class, 0, 255) + "&max=" + Utils.getRandomNumber(int.class, 0, 255);
        mockMvc.perform(get("/images/1?algorithm=extendDynamics" + args)).andExpect(status().isOk());
    }

    @Test
    @Order(18)
    public void equalizeHistogramShouldReturnSuccess() throws Exception{
        String args = "&x=" + Utils.getRandomNumber(int.class, 0, 2);
        mockMvc.perform(get("/images/1?algorithm=equalizeHistogram" + args)).andExpect(status().isOk());
    }

    @Test
    @Order(19)
    public void meanFilterShouldReturnSuccess() throws Exception{
        String args = "&radius=" + Utils.getRandomNumber(int.class, 0, 7);
        mockMvc.perform(get("/images/1?algorithm=meanFilter" + args)).andExpect(status().isOk());
    }

    @Test
    @Order(20)
    public void gaussianFilterShouldReturnSuccces() throws Exception {
        String args = "&radius=" + Utils.getRandomNumber(int.class, 0, 7);
        mockMvc.perform(get("/images/1?algorithm=gaussianFilter" + args)).andExpect(status().isOk());

    }

    @Test
    @Order(21)
    public void sobelOperatorShouldReturnSuccess() throws Exception{
        mockMvc.perform(get("/images/1?algorithm=sobelOperator")).andExpect(status().isOk());
    }

    @Test
    @Order(22)
    public void executeAlgorithmShouldReturnBadRequest() throws Exception{
        Set<String> listOfAlgorithms = AlgorithmManager.Instance().listAlgorithms();
        for (String name : listOfAlgorithms) {
            mockMvc.perform(get("/images/1?algorithm=" + name + "&x=1&y=2&z=5&delta=50")).andExpect(status().isBadRequest());
            mockMvc.perform(get("/images/1?algorithm=" + name.toUpperCase())).andExpect(status().isBadRequest());
            Set<Class<?>[]> parameterTypes = AlgorithmManager.Instance().listOfParameterType(name);
            for (Class<?>[] types : parameterTypes) {
                if(types.length > 2){
                    mockMvc.perform(get("/images/1?algorithm=" + name + "&x=Hello World !")).andExpect(status().isBadRequest());
                }
            }
        }
    }

    @Test
    @Order(23)
    public void executeAlgorithmShouldReturnNotFound() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();

        MvcResult result = mockMvc.perform(get("/images")).andReturn();
        String jsonData = result.getResponse().getContentAsString();
        List<Image> listImages = objectMapper.readValue(jsonData, new TypeReference<List<Image>>(){});
        Set<String> listOfAlgorithms = AlgorithmManager.Instance().listAlgorithms();
        for (String name : listOfAlgorithms) {
            mockMvc.perform(get("/images/" + (listImages.size()+1) +"?algorithm=" + name)).andExpect(status().isNotFound());
        }

    }

    @Disabled
    @Test
    public void TestPerformanceAlgorithmExecution() throws Exception{
        String parameters = "toGrayscale";
        long start = System.currentTimeMillis();
        for(int i = 0; i < 100; i++){
            mockMvc.perform(get("/images/0?algorithm=" + parameters));
        }
        System.out.println(System.currentTimeMillis() - start);
    }
}

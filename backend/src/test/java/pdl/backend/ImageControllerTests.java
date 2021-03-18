package pdl.backend;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.FileInputStream;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

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
        MockMultipartFile file = new MockMultipartFile("file", "osabat.jpg", MediaType.IMAGE_JPEG_VALUE,
                new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/osabat.jpg"));
        this.mockMvc.perform(multipart("/images").file(file)).andDo(print()).andExpect(status().isCreated());
    }

    @Test
    @Order(8)
    public void createImageShouldReturnUnsupportedMediaType() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "osabat.png", MediaType.IMAGE_PNG_VALUE,
                new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/osabat.png"));
        this.mockMvc.perform(multipart("/images").file(file)).andDo(print())
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @Order(9)
    public void listFilesShouldReturnSucess() throws Exception {
        Path path = Paths.get(System.getProperty("user.dir"), "/src/main/resources/images");
        Set<String> images = Utils.listFiles(path);
        assertTrue(images.size() != 0);
        images.forEach(ima -> assertTrue(ima.contains(".tif") || ima.contains(".jpeg")));
        images.forEach(s -> System.out.println(s));
    }
}

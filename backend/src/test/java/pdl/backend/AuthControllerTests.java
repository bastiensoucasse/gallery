package pdl.backend;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import pdl.backend.controller.ImageController;
import pdl.backend.mysqldb.ImageRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class AuthControllerTests {
    @Autowired
    private MockMvc mockMvc;

    // @Autowired
    // private ImageDAO imageDAO;

    @Autowired
    private ImageRepository imageRepository;


    @Autowired
    private ImageController imageController;

    @Test
    @Order(1)
    public void signinShouldReturnSuccess(){

    }

}
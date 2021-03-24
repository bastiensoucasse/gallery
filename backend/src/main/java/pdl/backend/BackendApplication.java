package pdl.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {
    public static void main(String[] args) {
        AlgorithmManager.Instance(); // initialize the singleton before the application is created
        SpringApplication.run(BackendApplication.class, args);
    }
}

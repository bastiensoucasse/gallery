package pdl.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {
    public static void main(final String[] args) {
        AlgorithmManager.Instance();
        SpringApplication.run(BackendApplication.class, args);
    }
}

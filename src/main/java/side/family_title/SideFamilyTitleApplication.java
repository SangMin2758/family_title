package side.family_title;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class SideFamilyTitleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SideFamilyTitleApplication.class, args);
    }

}

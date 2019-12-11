package spring.boot;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories(basePackages = {"spring.boot.repositories"})
@SpringBootApplication
public class Application {




    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }





}


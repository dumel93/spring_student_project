package week6.boot;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;


@EnableJpaRepositories(basePackages = { "week6.boot.repositories" })
@SpringBootApplication
public class Application {




    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }





}


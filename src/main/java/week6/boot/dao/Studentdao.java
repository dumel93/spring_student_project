package week6.boot.dao;

import com.github.javafaker.Faker;
import groovy.lang.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import week6.boot.entity.Student;
import week6.boot.repositories.StudentRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
@Transactional
public class Studentdao {


    @PersistenceContext
    private EntityManager entityManager;


    public void PopulateRandomStudents(){

        File file = new File("/Users/damiankrawczyk/Documents/week6/src/main/resources/application.properties");



        List<Student> students= new ArrayList<>();
        for (int i=0;i<10;i++){
            Faker faker = new Faker(new Locale("pl_PL"));

            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email=faker.internet().emailAddress();

            Student student = new Student();
            student.setFirstName(firstName);
            student.setLastName(lastName);
            student.setEmail(email);


            entityManager.persist(student);
            log.info(student.toString());

        }








    }




}

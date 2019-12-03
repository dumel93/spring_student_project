package week6.boot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import week6.boot.entity.Student;

import java.util.List;


@Component
public interface StudentRepository extends JpaRepository<Student,Long> {

    Student findByFirstName(String firstName);

    List<Student> findByLastNameIgnoreCase(String lastName);
    List<Student> findByLastNameAndFirstNameAllIgnoreCase(String lastName,
                                                          String firstName);


    Student findById(Long id);



}

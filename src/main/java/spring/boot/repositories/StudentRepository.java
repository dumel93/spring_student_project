package spring.boot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.boot.entity.Student;

import java.util.List;


@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {

    Student findByFirstName(String firstName);

    List<Student> findByLastNameIgnoreCase(String lastName);
    List<Student> findByLastNameAndFirstNameAllIgnoreCase(String lastName,
                                                          String firstName);



    Student findByEmail(String email);



    @Query(value = "select * from students where id !=1",nativeQuery = true)
    List<Student> findAllStudents();





}

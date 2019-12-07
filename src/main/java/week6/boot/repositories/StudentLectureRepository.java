package week6.boot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import week6.boot.entity.Student;
import week6.boot.entity.StudentLecture;

import java.util.List;

@Repository
public interface StudentLectureRepository extends JpaRepository<StudentLecture,Long> {



    @Query(value = "select is_present from student_lecture where student_id=?1", nativeQuery = true)
    List<Boolean> findAllByStudentIdOnList(Long  id);


    @Query(value = "select is_present from student_lecture where lecture_id=?1", nativeQuery = true)
    List<Boolean> findAllByLEctureIdOnList(Long  id);

    List<StudentLecture> findAllByStudentId(Long id);


    @Query(value = "select is_present from student_lecture where lecture_id=?1", nativeQuery = true)
    List<StudentLecture> findAllByLectureId(Long id);


    @Query(value = "select * from student_lecture where student_id=?1 and lecture_id=?2", nativeQuery = true)
    StudentLecture findByStudentIdAndLectureId(Long student_id, Long lecture_id);

    @Modifying
    @Query(value = "delete from student_lecture where student_id=?1", nativeQuery = true)
    void deleteStudentLecturesByStudentIdCustom(Long id);

}


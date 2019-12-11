package spring.boot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.boot.entity.StudentLecture;

import java.util.List;

@Repository
public interface StudentLectureRepository extends JpaRepository<StudentLecture,Long> {



    @Query(value = "select is_present from student_lecture join lectures on student_lecture.lecture_id=lectures.id where student_id=?1 order by date asc", nativeQuery = true)
    List<Boolean> findAllByStudentIdOnList(Long  id);


    @Query(value = "select is_present from student_lecture where lecture_id=?1", nativeQuery = true)
    List<Boolean> findAllByLEctureIdOnList(Long  id);


    @Query(value = "select * from student_lecture where student_id=?1", nativeQuery = true)
    List<StudentLecture> findAllByStudentId(Long id);


    @Query(value = "select * from student_lecture where lecture_id=?1", nativeQuery = true)
    List<StudentLecture> findAllByLectureId(Long id);


    @Query(value = "select * from student_lecture where student_id=?1 and lecture_id=?2", nativeQuery = true)
    StudentLecture findByStudentIdAndLectureId(Long student_id, Long lecture_id);


}


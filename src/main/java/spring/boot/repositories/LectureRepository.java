package spring.boot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.boot.entity.Lecture;

import java.util.List;


@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {


    public List<Lecture> findAllByOrderByDateAsc();

}

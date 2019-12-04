package week6.boot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import week6.boot.entity.Lecture;
import week6.boot.entity.Role;

import java.util.List;


@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {




}

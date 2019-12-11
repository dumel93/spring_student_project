package spring.boot.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class StudentLecture {


    @Id
    @GeneratedValue(strategy =
            GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;


    @ManyToOne
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    private boolean isPresent=false;

}

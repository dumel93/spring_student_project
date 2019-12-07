package week6.boot.entity;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import week6.boot.validation.Amount;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.groups.Default;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name="lectures")
public class Lecture {

//    @Amount(value = 8,message = "dear admin u cannot add more than 8 to list")
    @Id
    @GeneratedValue(strategy =
            GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(groups = {Default.class})
    @Column(
            length = 100,
            nullable = false)
    private String subject;



    @NotEmpty(groups = {Default.class})
    @Column(
            length = 100,
            nullable = false)
    private String location;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date date;
//
    @OneToMany(cascade = CascadeType.REMOVE,orphanRemoval = true)
//    @JoinColumn(name="id_student")
    private List<Student> students = new ArrayList<>();


}

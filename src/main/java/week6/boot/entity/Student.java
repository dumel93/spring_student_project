package week6.boot.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import week6.boot.validation.CustomEmailVal;

import javax.persistence.*;
import javax.validation.groups.Default;


@Data
@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy =
            GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(groups = {Default.class})
    @Column(
            length = 100,
            nullable = false)
    private String firstName;

    @NotEmpty(groups = {Default.class})
    @Column(
            length = 100,
            nullable = true)
    private String lastName;

    @NotEmpty(groups = {Default.class})
    @Column(unique = true)
    @CustomEmailVal
    private String email;


}

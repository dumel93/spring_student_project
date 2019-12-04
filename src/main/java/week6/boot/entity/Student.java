package week6.boot.entity;

import com.sun.istack.internal.Nullable;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import week6.boot.validation.CustomEmailVal;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Data
@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy =
            GenerationType.IDENTITY)
    private Long id;


    @Column(
            length = 100,
            nullable = false)
    private String firstName;


    @Column(
            length = 100,
            nullable = true)
    private String lastName;


    private boolean isPresent=false;

    @NotNull
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.ALL})
    @JoinTable(name = "student_lecture", joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "lecture_id"))
    private List<Lecture> lectures = new ArrayList<>();

    @Email(message = "*Please provide a valid Email")
    @Column(nullable = false, unique = true)
    private String email;


    @Column(name = "password")
    @Length(min = 5, message = "*Your password must have at least 5 characters")
    private String password;

    @Column(name = "active")
    private boolean active=false;

    @Nullable
    @ManyToMany( fetch = FetchType.EAGER)
    @JoinTable(name = "student_role", joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
    //
    @Transient
    private String confirmPassword;

    public void setPassword(String password) {
        this.password = password;
        checkPassword();//check
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        checkPassword();//check
    }

    private void checkPassword() {
        if(this.password == null || this.confirmPassword == null){
            return;
        }else if(!this.password.equals(confirmPassword)){
            this.confirmPassword = null;
        }
    }

}

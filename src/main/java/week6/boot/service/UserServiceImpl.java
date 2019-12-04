package week6.boot.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import week6.boot.entity.Role;
import week6.boot.entity.Student;
import week6.boot.repositories.RoleRepository;
import week6.boot.repositories.StudentRepository;

import java.util.Arrays;
import java.util.HashSet;

@Service
public class UserServiceImpl implements  UserService {

    private final StudentRepository studentRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(StudentRepository studentRepository,
                           RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.studentRepository=studentRepository;
    }

    @Override
    public Student findByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    @Override
    public void saveUser(Student student)  {

        student.setPassword(passwordEncoder.encode(student.getPassword()));
        student.setActive(true);
        Role userRole = roleRepository.findByName("ROLE_STUDENT");
        student.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        studentRepository.save(student);



    }

    @Override
    public void updateUser(Student student) {

        student.setId(student.getId());
        student.setFirstName(student.getFirstName());
        student.setLastName(student.getLastName());
        student.setEmail(student.getEmail());
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        student.setActive(true);
        Role userRole = roleRepository.findByName("ROLE_STUDENT");
        student.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        studentRepository.save(student);

    }
}

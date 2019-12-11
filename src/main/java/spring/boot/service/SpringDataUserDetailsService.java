package spring.boot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import spring.boot.entity.Role;
import spring.boot.entity.Student;
import spring.boot.repositories.StudentRepository;

import java.util.*;

@Slf4j
public class SpringDataUserDetailsService implements UserDetailsService {

    private UserService userService;

    @Autowired
    private StudentRepository studentRepository;



    @Override
    public UserDetails loadUserByUsername(String email) {
        Student user = studentRepository.findByEmail(email);
        log.info(user.toString());

        if (user == null) {throw new UsernameNotFoundException(email); }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : user.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        Student student=new Student();
        student.setId(user.getId());
        student.setFirstName(user.getFirstName());
        student.setLastName(user.getLastName());
//        student.setPresent(user.isPresent());
        student.setActive(true);
        student.setLectures(user.getLectures());

        return new CurrentUser(user.getEmail(),user.getPassword(),

                grantedAuthorities, student);



    }

}

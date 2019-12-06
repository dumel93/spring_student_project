package week6.boot.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import week6.boot.entity.Lecture;
import week6.boot.entity.Student;
import week6.boot.repositories.LectureRepository;
import week6.boot.repositories.StudentRepository;

import java.util.List;

@Controller
public class HomeController {


    @Autowired
    private StudentRepository studentRepository;


    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal UserDetails user){


        String emailCuurentUser=user.getUsername();
        Student student = studentRepository.findByEmail(emailCuurentUser);

        model.addAttribute("student",student);

        return "home";
    }
}

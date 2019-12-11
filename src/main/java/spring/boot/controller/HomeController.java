package spring.boot.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import spring.boot.entity.Student;
import spring.boot.repositories.StudentRepository;



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

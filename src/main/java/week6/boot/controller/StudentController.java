package week6.boot.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import week6.boot.entity.Student;
import week6.boot.repositories.StudentRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Controller
@Transactional
public class StudentController {
    @Autowired
    private StudentRepository studentRepository;


    @RequestMapping("/index")

    public String home(Model model) {

        List<Student> students= studentRepository.findAll();

        model.addAttribute("students",students);


        return "index";
    }


}

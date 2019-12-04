package week6.boot.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import week6.boot.entity.Student;
import week6.boot.repositories.StudentRepository;
import week6.boot.service.UserService;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@Controller
@Slf4j
@Transactional
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserService userService;



    @RequestMapping(value = "/get", method=RequestMethod.GET)
    public String home(Model model) {

        List<Student> students= studentRepository.findAllStudents();
        model.addAttribute("students",students);


        return "student/studentPage";
    }


    @RequestMapping(value="/add", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        Student user = new Student();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("student/studentAdd");
        return modelAndView;
    }


    @RequestMapping(value="/add", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid Student user, BindingResult bindingResult ) {

        ModelAndView modelAndView = new ModelAndView();
        Student userExists = studentRepository.findByEmail(user.getEmail());
//
//        if (userExists != null) {
//            bindingResult
//                    .rejectValue("email", "error.user",
//                            "* There is already a user registered with the email provided");
//
//        }
//
//
////        else if (user.getPassword().length() == 0) {
////            bindingResult.rejectValue("password", "error.user", "* cannot be empty ");
////        }
////
////        else if (user.getFirstName().length()==0) {
////            bindingResult.rejectValue("firstName", "error.user", "* cannot be empty ");
////        }
////
////        else if (user.getLastName().length()==0) {
////            bindingResult.rejectValue("lastName", "error.user", "* cannot be empty ");
////        }
//
//
//        else if (!user.getPassword().equals(user.getConfirmPassword())) {
//
//            bindingResult.rejectValue("confirmPassword", "error.user",
//                    "*please retype a password, incorrect password, they are not the same");
//        }




//        else {



            userService.saveUser(user);
            modelAndView.addObject("successMessage", "User: "
                    +user.getFirstName() + " " + user.getLastName()+ " "
                    + user.getEmail() + " has been registered successfully");
            modelAndView.addObject("students", studentRepository.findAllStudents());

            modelAndView.setViewName("student/studentPage");
//        }


        return modelAndView;
    }


    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(Model model, @RequestParam Long id) {


        Student student =studentRepository.findOne(id);

        String name=student.getFirstName();
        String lastName=student.getLastName();
        studentRepository.delete(id);


        model.addAttribute("successMessage","It was removed student:: "+ name+" "+lastName);
        model.addAttribute("students",studentRepository.findAllStudents());

        return "student/studentPage";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editGET(@RequestParam Long id,Model model) {
        Student student=studentRepository.findOne(id);
        model.addAttribute("student",student);

        return "student/studentEdit";
    }


    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public String editPOST(@Valid Student updatedStudent, BindingResult result,Model model) {

        if(result.hasErrors()){
            return "student/studentEdit";
        }


        userService.updateUser(updatedStudent);
        String name=updatedStudent.getFirstName();
        String lastName=updatedStudent.getLastName();
        List<Student> students= studentRepository.findAllStudents();
        model.addAttribute("students",students);
        model.addAttribute("successMessage","It was updated student:: "+ name+" "+lastName);
        return "student/studentPage";
    }






}

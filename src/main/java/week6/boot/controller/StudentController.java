package week6.boot.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import week6.boot.entity.Lecture;
import week6.boot.entity.Student;
import week6.boot.repositories.LectureRepository;
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

    @Autowired
    private LectureRepository lectureRepository;



    @RequestMapping(value = "/get", method=RequestMethod.GET)
    public String home(Model model) {

        List<Student> students= studentRepository.findAllStudents();
        model.addAttribute("students",students);


        return "student/studentPage";
    }


    @RequestMapping(value="/add", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        Student student = new Student();
        modelAndView.addObject("student", student);
        modelAndView.setViewName("student/studentAdd");
        return modelAndView;
    }


    @RequestMapping(value="/add", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid Student student, BindingResult bindingResult ) {

        ModelAndView modelAndView = new ModelAndView();

        Student userExists = studentRepository.findByEmail(student.getEmail());

        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "* There is already a user registered with the email provided");
        }


        else if (!student.getPassword().equals(student.getConfirmPassword())) {

            bindingResult.rejectValue("confirmPassword", "error.user",
                    "*please retype a password, incorrect password, they are not the same");
        }

        else {

            userService.saveUser(student);
            modelAndView.addObject("successMessage", "Student: "
                    +student.getFirstName() + " " + student.getLastName()+ " "
                    + student.getEmail() + " has been registered successfully");
            modelAndView.addObject("students", studentRepository.findAllStudents());

            modelAndView.setViewName("student/studentPage");
            return modelAndView;
        }

        modelAndView.setViewName("student/studentAdd");
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
    public String editPOST(@PathVariable long id,@Valid Student updatedStudent, BindingResult bindingResult,Model model) {

        String poteniclaEmailtoSave = updatedStudent.getEmail();

        Student realEditedStudent =studentRepository.findOne(id);

        Student studentinDB= studentRepository.findByEmail(poteniclaEmailtoSave);


        if(!realEditedStudent.getEmail().equals(poteniclaEmailtoSave)){

            if(studentinDB!= null){
                bindingResult
                        .rejectValue("email", "error.student",
                                "* There is already a user registered with the email provided");
                return "student/studentEdit";
            }

            else{
                if (!updatedStudent.getPassword().equals(updatedStudent.getConfirmPassword())) {

                bindingResult.rejectValue("confirmPassword", "error.user",
                        "*please retype a password, incorrect password, they are not the same");
                return "student/studentEdit";
            }

                    userService.updateUser(updatedStudent);
                    String fistname = updatedStudent.getFirstName();
                    List<Student> students = studentRepository.findAllStudents();
                    model.addAttribute("students", students);
                    model.addAttribute("successMessage", "It was updated student:: " + fistname + " " +
                            "and it has been set new email  on : " + poteniclaEmailtoSave);

                    return "student/studentPage";

            }

        }

        else{
            if (!updatedStudent.getPassword().equals(updatedStudent.getConfirmPassword())) {

                bindingResult.rejectValue("confirmPassword", "error.user",
                        "*please retype a password, incorrect password, they are not the same");
            } else {


                userService.updateUser(updatedStudent);
                String name = updatedStudent.getFirstName();
                String lastName = updatedStudent.getLastName();
                List<Student> students = studentRepository.findAllStudents();
                model.addAttribute("students", students);
                model.addAttribute("successMessage", "It was updated student:: " + name + " " + lastName);
                return "student/studentPage";
            }

            return "student/studentEdit";
        }



    }




    @RequestMapping(value = "/list", method=RequestMethod.GET)
    public String attendanceList(Model model) {

        List<Student> students= studentRepository.findAllStudents();
        List<Lecture> lectures=lectureRepository.findAllByOrderByDateAsc();

        int howManyLectureStudentWas=0;
        int howManyStudentsWereonLecture=0;



        for(Student student: students){
            if (student.isPresent()){
                howManyStudentsWereonLecture += 1;
            }

        }

        for(Lecture lecture:lectures){

            for(Student student:lecture.getStudents()){
                if(student.isPresent()){
                    howManyLectureStudentWas += 1;
                    break;

                }
            }

        }



        model.addAttribute("howManyLectureStudentWas",String.valueOf(howManyLectureStudentWas));
        model.addAttribute("howManyStudentsWereonLecture",String.valueOf(howManyStudentsWereonLecture));
        model.addAttribute("students",students);
        model.addAttribute("lectures",lectures);


        return "attendance/attendanceList";
    }

    @EventListener(ApplicationReadyEvent.class)
    public void addStudentstoLecture() {

        List<Student> students= studentRepository.findAllStudents();
        List<Lecture> lectures=lectureRepository.findAllByOrderByDateAsc();

        for(Student student: students){
            student.setLectures(lectures);
            student.setPresent(false); // change later
            studentRepository.save(student);

        }
    }


    @EventListener(ApplicationReadyEvent.class)
    public void setUpPassForAllusers() {


        // admin has default pass:  'admin123' , all users have  default pass :'user123' all is bcrypted in db
        List<Student> users= studentRepository.findAllStudents();

        for(Student user:users){
            user.setPassword("user123");
            userService.saveUser(user);
        }



    }






}

package week6.boot.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import week6.boot.entity.Lecture;
import week6.boot.entity.Student;
import week6.boot.entity.StudentLecture;
import week6.boot.repositories.LectureRepository;
import week6.boot.repositories.StudentLectureRepository;
import week6.boot.repositories.StudentRepository;
import week6.boot.service.UserService;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private StudentLectureRepository studentLectureRepository;

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/get", method=RequestMethod.GET)
    public String home(Model model) {

        List<Student> students= studentRepository.findAllStudents();
        model.addAttribute("students",students);


        return "student/studentPage";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value="/add", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        Student student = new Student();
        modelAndView.addObject("student", student);
        modelAndView.setViewName("student/studentAdd");
        return modelAndView;
    }


    @Secured("ROLE_ADMIN")
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
            for (Lecture lecture : lectureRepository.findAll()) {
                StudentLecture sl = new StudentLecture();
                sl.setPresent(false);
                sl.setLecture(lecture);
                sl.setStudent(student);
                studentLectureRepository.save(sl);
            }

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

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(Model model, @RequestParam Long id, @AuthenticationPrincipal UserDetails user) {


        Student student =studentRepository.findOne(id);

        String name=student.getFirstName();
        String lastName=student.getLastName();


        String email = user.getUsername();
        Student student1= studentRepository.findByEmail(email);

        List<StudentLecture> studentLectureList= studentLectureRepository.findAllByStudentId(student.getId());
        for( StudentLecture studentLectureToDelete:studentLectureList ){
            studentLectureRepository.delete(studentLectureToDelete);
        }
        studentRepository.delete(student);



        model.addAttribute("successMessage","It was removed student:: "+ name+" "+lastName);
        model.addAttribute("students",studentRepository.findAllStudents());

        return "student/studentPage";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editGET(@RequestParam Long id,Model model) {
        Student student=studentRepository.findOne(id);
        model.addAttribute("student",student);

        return "student/studentEdit";
    }

    @Secured("ROLE_ADMIN")
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


    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/list", method=RequestMethod.GET)
    public String attendanceList(Model model) {

        List<Student> students= studentRepository.findAllStudents();
        List<Lecture> lectures=lectureRepository.findAllByOrderByDateAsc();



        List<List<Boolean>> listOflistByLecture=new ArrayList<>();
        List<List<Boolean>> listOflistByStudent=new ArrayList<>();



        for(Lecture lecture: lectures){
            listOflistByLecture.add(studentLectureRepository.findAllByLEctureIdOnList(lecture.getId()));
        }

        for(Student student: students){
            listOflistByStudent.add(studentLectureRepository.findAllByStudentIdOnList(student.getId()));
        }



        List<Integer> howManycLectureStudentWas = new ArrayList<>();
        List<Integer> howManyStudentsWereonLecture = new ArrayList<>();


        howManycLectureStudentWas=this.countPosnumberInListofList(listOflistByLecture);
        howManyStudentsWereonLecture=this.countPosnumberInListofList(listOflistByStudent);


        List<String> StringhowManycLectureStudentWas = howManycLectureStudentWas.stream().map(Object::toString)
                .collect(Collectors.toList());

        List<String> StringhowManyStudentsWereonLecture=howManyStudentsWereonLecture.stream().map(Object::toString)
                .collect(Collectors.toList());



        model.addAttribute("lectureCounter",StringhowManycLectureStudentWas);
        model.addAttribute("studentCounter",StringhowManyStudentsWereonLecture);

        model.addAttribute("list",listOflistByLecture);
        model.addAttribute("students",students);
        model.addAttribute("lectures",lectures);


        return "attendance/attendanceList";
    }

    @EventListener(ApplicationReadyEvent.class)
    public void addStudentstoLecture() {



        List<Student> students= studentRepository.findAllStudents();
        List<Lecture> lectures=lectureRepository.findAllByOrderByDateAsc();



        for(int i=0;i<students.size();i++){
            for(int j=0;j<lectures.size();j++){
                if((j%2==0 || i%3 ==0)&& (lectures.get(j).getDate().after(new Date()))!=true){
                    StudentLecture sl = new StudentLecture();

                    sl.setPresent(true);
                    sl.setLecture(lectures.get(j));
                    sl.setStudent(students.get(i));
                    studentLectureRepository.save(sl);

                }//random
                else {
                    StudentLecture sl = new StudentLecture();
                    sl.setPresent(false);
                    sl.setLecture(lectures.get(j));
                    sl.setStudent(students.get(i));
                    studentLectureRepository.save(sl);
                    studentLectureRepository.save(sl);
                }

            }
        }
    }




    @EventListener(ApplicationReadyEvent.class)
    public void setUpPassForAllusers() {


        // admin has default pass:  'admin123' , all users have  default pass :'user123' all is bcrypted in db
        List<Student> users= studentRepository.findAllStudents();
        List<Student> users2 = studentRepository.findAll();
        Student admin = users2.get(0);
        admin.setPassword("admin123");
        userService.saveAdmin(admin);

        for(Student user:users){
            user.setPassword("user123");
            userService.saveUser(user);
        }


    }

    public List<Integer> countPosnumberInListofList(List<List<Boolean>> listofLIst){



        List<Integer> attendance = new ArrayList<>();

        for(List<Boolean> list:listofLIst){
            attendance.add(countPosnumber(list));
        }

        return attendance;

    }

    public int countPosnumber(List<Boolean> list){

        int attendance = 0;

        for(Boolean val:list){
            if(val) attendance +=1;
        }

        return attendance;

    }

}

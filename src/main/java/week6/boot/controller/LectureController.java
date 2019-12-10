package week6.boot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/lecture")
public class LectureController {

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private  StudentLectureRepository studentLectureRepository;

    @Secured("ROLE_ADMIN")
    @GetMapping("/get")
    public String home(Model model) {

        List<Lecture> lectures= lectureRepository.findAllByOrderByDateAsc();
        model.addAttribute("lectures",lectures);
        return "lecture/lectureList";

    }

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("lectures", lectureRepository.findAllByOrderByDateAsc());
    }






    @Secured("ROLE_STUDENT")
    @GetMapping("/regAtt")
    public String registerAttGet(Model model,@AuthenticationPrincipal UserDetails user){


        List<Lecture> lectures= lectureRepository.findAllByOrderByDateAsc();
        model.addAttribute("lectures",lectures);


        String emailCuurentUser=user.getUsername();
        Student student = studentRepository.findByEmail(emailCuurentUser);

        model.addAttribute("student",student);

        List<Boolean> booleanList= studentLectureRepository.findAllByStudentIdOnList(student.getId());

        model.addAttribute("booleanList",booleanList);

        int attendance =this.countPosnumber(booleanList);


        model.addAttribute("attendance",String.valueOf(attendance));


        return "lecture/RegisterAttendanceByStudent";
    }



    @Secured("ROLE_STUDENT")
    @PostMapping("/regAtt/{id}")
    public String registerAttPost(@PathVariable long id, HttpServletRequest request, Model model, @ModelAttribute Student student, @AuthenticationPrincipal UserDetails user) {


        String value= request.getParameter("present");
        Boolean bool=Boolean.valueOf(value);

        String email =user.getUsername();
        Student student2=studentRepository.findByEmail(email);


        List<Lecture> lectures= lectureRepository.findAllByOrderByDateAsc();
        model.addAttribute("lectures",lectures);

        Long student_id=student2.getId();

        StudentLecture sl=  studentLectureRepository.findByStudentIdAndLectureId(student_id,id);
        sl.setPresent(bool);
//        sl.setId(sl.getId());
//        sl.setStudent(sl.getStudent());
//        sl.setLecture(sl.getLecture());
        studentLectureRepository.save(sl);


        model.addAttribute("student",student2);

        List<Boolean> newList= studentLectureRepository.findAllByStudentIdOnList(student_id);
        model.addAttribute("booleanList",newList);



        model.addAttribute("attendance",String.valueOf(this.countPosnumber(newList)));

        if(bool)model.addAttribute("success","you have just registered your attendance: lecture "+ lectureRepository.findOne(id).getSubject());


        return "lecture/RegisterAttendanceByStudent";
    }


    @Secured("ROLE_ADMIN")
    @RequestMapping(value="/add", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        Lecture lecture = new Lecture();
        modelAndView.addObject("lecture", lecture);
        modelAndView.setViewName("lecture/lectureAdd");
        return modelAndView;
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processForm(@Valid Lecture lecture, BindingResult bindingResult,Model model) {

        List<Lecture> lectures = lectureRepository.findAllByOrderByDateAsc();
        lectures.add(lecture);
        if (lectures.size() > 8) {

            bindingResult
                    .rejectValue("date", "error.user",
                            "* dear admin you cannot add to list more than 8 lectures ");
            return "lecture/lectureAdd";
        } else {

            lectureRepository.save(lecture);

            for (Student student : studentRepository.findAllStudents()) {
                StudentLecture sl = new StudentLecture();
                sl.setPresent(false);
                sl.setLecture(lecture);
                sl.setStudent(student);
                studentLectureRepository.save(sl);
            }
            model.addAttribute("successMessage", "Created new lecture: " + lecture.getSubject());
            model.addAttribute("lectures", lectures);

            return "lecture/lectureList";

        }



    }


    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editGET(@RequestParam Long id, Model model) {
        Lecture lecture=lectureRepository.findOne(id);
        model.addAttribute("lecture",lecture);

        return "lecture/lectureEdit";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public String editPOST(@PathVariable long id, @Valid Lecture lecture, Model model) {

        Lecture updatedLecture=lectureRepository.findOne(id);
        updatedLecture.setId(id);
        updatedLecture.setSubject(lecture.getSubject());
        updatedLecture.setLocation(lecture.getLocation());
        updatedLecture.setDate(lecture.getDate());

        lectureRepository.save(updatedLecture);
        String name=updatedLecture.getSubject();

        List<Lecture> lectures= lectureRepository.findAllByOrderByDateAsc();
        model.addAttribute("lectures",lectures);
        model.addAttribute("successMessage","It was updated lecture:: "+ name);
        return "lecture/lectureList";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(Model model, @RequestParam Long id) {


        Lecture lecture = lectureRepository.findOne(id);

        if (lecture.getDate().after(new Date())) {
            String name = lecture.getSubject();
            List<StudentLecture> studentLectureList = studentLectureRepository.findAllByLectureId(id);
            for (StudentLecture studentLectureToDelete : studentLectureList) {
                studentLectureRepository.delete(studentLectureToDelete);
            }
            lectureRepository.delete(id);
            model.addAttribute("successMessage", "It was removed lecture:: " + name);
            model.addAttribute("lectures", lectureRepository.findAllByOrderByDateAsc());



            return "lecture/lectureList";
        } else {
            model.addAttribute("error","admin, you cannot delete a lecture that were already started!");
            List<Lecture> lectures= lectureRepository.findAllByOrderByDateAsc();
            model.addAttribute("lectures",lectures);

            return "lecture/lectureList";
            }


        }

    public int countPosnumber(List<Boolean> list){

        int attendance = 0;

        for(Boolean val:list){
            if(val) attendance +=1;
        }

        return attendance;

    }

}

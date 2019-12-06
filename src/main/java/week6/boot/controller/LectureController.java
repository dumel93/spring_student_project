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
import week6.boot.repositories.LectureRepository;
import week6.boot.repositories.StudentRepository;
import week6.boot.service.CurrentUser;

import javax.validation.Valid;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/lecture")
public class LectureController {

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/get")
    public String home(Model model) {

        List<Lecture> lectures= lectureRepository.findAllByOrderByDateAsc();
        model.addAttribute("lectures",lectures);
        return "lecture/lectureList";

    }


    @Secured("ROLE_STUDENT")
    @GetMapping("/regAtt")
    public String registerAttGet(Model model,@AuthenticationPrincipal UserDetails user){


        List<Lecture> lectures= lectureRepository.findAllByOrderByDateAsc();
        model.addAttribute("lectures",lectures);


        String emailCuurentUser=user.getUsername();
        Student student = studentRepository.findByEmail(emailCuurentUser);

        model.addAttribute("student",student);

        return "lecture/RegisterAttendanceByStudent";
    }

    @Secured("ROLE_STUDENT")
    @PostMapping("/regAtt")
    public String registerAttPost(Model model,@Valid Student student){




//        List<Lecture> lectures= lectureRepository.findAllByOrderByDateAsc();
//        for(Lecture lecture: lectures){
//
//            if(student.getEmail().equals(lecture.getStudents()))
//        }

        return "lecture/RegisterAttendanceByStudent";
    }



    @RequestMapping(value="/add", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        Lecture lecture = new Lecture();
        modelAndView.addObject("lecture", lecture);
        modelAndView.setViewName("lecture/lectureAdd");
        return modelAndView;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processForm(@Valid Lecture lecture, BindingResult bindingResult,Model model) {

        List<Lecture> lectures= lectureRepository.findAllByOrderByDateAsc();
        lectures.add(lecture);
        if(lectures.size()>8){

           bindingResult
                  .rejectValue("date", "error.user",
                           "* dear admin you cannot add to list more than 8 lectures ");
        } else {
            lectureRepository.save(lecture);


            model.addAttribute("successMessage", "Created new lecture: "+ lecture.getSubject());
            model.addAttribute("lectures",lectures);

            return "lecture/lectureList";
        }

        return "lecture/lectureAdd";
    }


    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editGET(@RequestParam Long id, Model model) {
        Lecture lecture=lectureRepository.findOne(id);
        model.addAttribute("lecture",lecture);

        return "lecture/lectureEdit";
    }


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

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(Model model, @RequestParam Long id) {


        Lecture lecture =lectureRepository.findOne(id);

        if (lecture.getDate().after(new Date())) {
            String name=lecture.getSubject();
            lectureRepository.delete(id);


            model.addAttribute("successMessage","It was removed lecture:: "+ name);
            model.addAttribute("lectures",lectureRepository.findAllByOrderByDateAsc());

            return "lecture/lectureList";
        } else {
           model.addAttribute("error","admin, you cannot delete a lecture that has not yet started!");
            List<Lecture> lectures= lectureRepository.findAllByOrderByDateAsc();
            model.addAttribute("lectures",lectures);

            return "lecture/lectureList";
        }



    }

}

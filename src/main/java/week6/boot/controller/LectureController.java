package week6.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import week6.boot.entity.Lecture;
import week6.boot.entity.Student;
import week6.boot.repositories.LectureRepository;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/")
public class LectureController {

    @Autowired
    private LectureRepository lectureRepository;

    @GetMapping("lecture/get")
    public String home(Model model) {

        List<Lecture> lectures= lectureRepository.findAll();
        model.addAttribute("lectures",lectures);

        return "lecture/lectureList";
    }

    @RequestMapping(value="lecture/add", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        Lecture lecture = new Lecture();
        modelAndView.addObject("lecture", lecture);
        modelAndView.setViewName("lecture/lectureAdd");
        return modelAndView;
    }

    @RequestMapping(value = "lecture/add", method = RequestMethod.POST)
    public String processForm(@Valid Lecture lecture, BindingResult bindingResult,Model model) {

        List<Lecture> lectures= lectureRepository.findAll();
        lectures.add(lecture);
        if(lectures.size()>8){

           bindingResult
                  .rejectValue("date", "error.user",
                           "* dear admin you cannot add to list more than 8 subjects ");
        } else {
            lectureRepository.save(lecture);


            model.addAttribute("successMessage", "Created new lecture: "+ lecture.getSubject());
            model.addAttribute("lectures",lectures);

            return "lecture/lectureList";
        }

        return "lecture/lectureAdd";
    }
}

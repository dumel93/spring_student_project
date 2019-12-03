package week6.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import week6.boot.dao.Studentdao;
import week6.boot.repositories.StudentRepository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

//@Component
public class DataLoader {


    @Autowired
    private Studentdao studentdao;

    //method invoked during the startup
    @PostConstruct
    public void loadData() {
        studentdao.PopulateRandomStudents();
    }





}

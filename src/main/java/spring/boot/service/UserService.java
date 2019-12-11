package week6.boot.service;

import week6.boot.entity.Student;

public interface UserService {
    public Student findByEmail(String name);
    public void saveUser(Student student);

    public void updateUser(Student student);

    public void saveAdmin(Student student);
}

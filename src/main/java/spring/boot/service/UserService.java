package spring.boot.service;

import spring.boot.entity.Student;

public interface UserService {
    public Student findByEmail(String name);
    public void saveUser(Student student);

    public void updateUser(Student student);

    public void saveAdmin(Student student);
}

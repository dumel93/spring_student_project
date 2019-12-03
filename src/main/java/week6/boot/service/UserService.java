package week6.boot.service;

import week6.boot.entity.User;

public interface UserService {
    public User findByEmail(String name);
    public void saveUser(User user);
}

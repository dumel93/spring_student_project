package week6.boot.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import week6.boot.entity.Student;

import java.util.Collection;

public class CurrentUser extends User implements UserDetails {


    private final week6.boot.entity.Student user;

    public CurrentUser(String username, String password, Collection<? extends GrantedAuthority> authorities, week6.boot.entity.Student user)
    {
        super(username, password, authorities);
        this.user = user;
    }
    public  week6.boot.entity.Student getUser() {
        return user;
    }


}

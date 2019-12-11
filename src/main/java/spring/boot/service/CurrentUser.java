package spring.boot.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CurrentUser extends User implements UserDetails {


    private final spring.boot.entity.Student user;

    public CurrentUser(String username, String password, Collection<? extends GrantedAuthority> authorities, spring.boot.entity.Student user)
    {
        super(username, password, authorities);
        this.user = user;
    }
    public  spring.boot.entity.Student getUser() {
        return user;
    }


}

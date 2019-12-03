package week6.boot.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CurrentUser extends User
{
    private final week6.boot.entity.User user;
    public CurrentUser(String email, String password, Collection<?
                extends GrantedAuthority> authorities,
                       week6.boot.entity.User user ) {
        super(email, password, authorities); this.user = user;
    }
    public week6.boot.entity.User getUser() {return user;}
}


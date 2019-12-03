package week6.boot.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailsService extends org.springframework.security.core.userdetails.UserDetailsService {
    UserDetails loadUserByUsername(String s) throws UsernameNotFoundException ;
    UserDetails loadUserByEmail(String s) throws UsernameNotFoundException ;
}

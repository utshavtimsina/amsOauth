package com.aeon.ams.config.auth;

import com.aeon.ams.model.User;
import com.aeon.ams.repo.MongoRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final MongoRepo repo;

    public UserDetailsServiceImpl(PasswordEncoder passwordEncoder , MongoRepo repo) {
        this.passwordEncoder = passwordEncoder;
        this.repo = repo;

    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
           User user = repo.findByUsername(s);
//        System.out.println(user.toString());
            if(user != null){
                return new CustomUserDetails(user);
            }
            throw new UsernameNotFoundException("Provided Credentials do not match");
    }
}
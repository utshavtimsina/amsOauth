package com.aeon.ams.config.auth;

import com.aeon.ams.repo.MongoRepo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final MongoRepo repo;

    public UserDetailsServiceImpl(PasswordEncoder passwordEncoder , MongoRepo repo) {
        this.passwordEncoder = passwordEncoder;
        this.repo = repo;

    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        com.aeon.ams.model.User user = repo.findByUsername(username);
        if(user != null){
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            user.getRoles().forEach(roles -> {
                authorities.add( new SimpleGrantedAuthority(roles.getRole()));
            });
            return new User(user.getUsername(), user.getPassword(), authorities );
        }
        throw new UsernameNotFoundException("Provided Credentials do not match");
    }
}
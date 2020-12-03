package com.aeon.ams.service.impl;

import com.aeon.ams.model.Roles;
import com.aeon.ams.model.User;
import com.aeon.ams.repo.MongoRepo;
import com.aeon.ams.service.UserService;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {
    private MongoRepo repo;
    private final MailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    public UserServiceImpl(MongoRepo repo, MailSender mailSender, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAll() {
        return repo.findAll();
    }

    @Override
    public boolean createStudent(User user) {
        if(repo.findByUsername(user.getUsername())!=null)
            return false;
        Random rand = new Random();
        String s = Base64.getEncoder().encodeToString(Double.toString(rand.nextDouble()).getBytes());
        System.out.print(s);
        User createUser = User.builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .activators(s)
                .isActive(false)
                .roles(Arrays.asList( Roles.builder().role("STUDENT").build()))
                .build();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getUsername());
        message.setSubject("AMS activation");
        message.setText(s);
        mailSender.send(message);
        repo.save(createUser);
        return true;
    }
}

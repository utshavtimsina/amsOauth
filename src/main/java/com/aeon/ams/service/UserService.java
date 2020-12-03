package com.aeon.ams.service;

import com.aeon.ams.model.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {
    List<User> getAll();

    boolean createStudent(User user);
}

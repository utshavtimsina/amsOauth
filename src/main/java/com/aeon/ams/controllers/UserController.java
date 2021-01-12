package com.aeon.ams.controllers;

import com.aeon.ams.model.User;
import com.aeon.ams.service.UserService;
import com.aeon.ams.utils.GlobalApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @GetMapping("/getAll")
//    public ResponseEntity<?> getAllUsers(){
//        return new ResponseEntity(new GlobalApiResponse(true,"success",userService.getAll(),false),HttpStatus.OK);
//    }

    @PostMapping("/register")
    public ResponseEntity<?> registerNewStudent(@Valid @RequestBody User user) throws Exception {
        if(userService.createStudent(user)){
            return new ResponseEntity( userService.createStudent(user),HttpStatus.OK);
        }
       throw new Exception("Username Already exist");
    }

    @PostMapping("/activate/{id}")
    public ResponseEntity<?> activateUser(@PathVariable String id){
        return new ResponseEntity( "",HttpStatus.OK);
    }

}

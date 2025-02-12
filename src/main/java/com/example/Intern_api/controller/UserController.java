package com.example.Intern_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Intern_api.model.LoginRequest;
import com.example.Intern_api.model.ResponseObject;
import com.example.Intern_api.model.User;
import com.example.Intern_api.service.UserService;

@RestController
@RequestMapping("/at/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseObject> registerUser(@RequestBody User user) {
        ResponseObject responseObject = userService.registerUser(user);
        if (responseObject.getStatus().equals("failed"))
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(responseObject);

        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<ResponseObject> getAllUser() {
        ResponseObject responseObject = userService.getAllUsers();
        if (responseObject.getStatus().equals("failed"))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseObject);

        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @GetMapping("/getUserById/{userId}")
    public ResponseEntity<ResponseObject> getUserById(@PathVariable Long userId) {
        ResponseObject responseObject = userService.getUserByUserId(userId);
        if (responseObject.getStatus().equals("failed"))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseObject);

        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @DeleteMapping("/deleteUserById/{userId}")
    public ResponseEntity<ResponseObject> deleteUserById(@PathVariable Long userId) {
        ResponseObject responseObject = userService.deleteUserByUserId(userId);
        if (responseObject.getStatus().equals("failed"))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseObject);

        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @PutMapping("/updateUserById/{userId}")
    public ResponseEntity<ResponseObject> updateUserById(@PathVariable Long userId, @RequestBody User user) {
        ResponseObject responseObject = userService.updateUserByUserId(user, userId);
        if (responseObject.getStatus().equals("failed"))
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(responseObject);

        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseObject> loginUser(@RequestBody LoginRequest loginRequest){
        ResponseObject userFound = userService.loginUser(loginRequest.getUserName(), loginRequest.getPassword());
        if (userFound.getStatus().equals("failed")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(userFound);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(userFound);
        }
    }
}

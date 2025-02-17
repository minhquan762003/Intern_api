package com.example.Intern_api.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @PreAuthorize("permitAll()")
    public ResponseEntity<ResponseObject> registerUser(@RequestBody User user, @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        ResponseObject responseObject = userService.registerUser(user, locale);
        if (responseObject.getStatus().equals("failed"))
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(responseObject);

        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ResponseObject> loginUser(@RequestBody LoginRequest loginRequest, @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        ResponseObject userFound = userService.loginUser(loginRequest.getUserName(), loginRequest.getPassword(), locale);
        if (userFound.getStatus().equals("failed")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(userFound);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(userFound);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/getAllUsers")
    public ResponseEntity<ResponseObject> getAllUser(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        ResponseObject responseObject = userService.getAllUsers(page, size, locale);
        if (responseObject.getStatus().equals("failed"))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseObject);

        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @GetMapping("/getUserById/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseObject> getUserById(@PathVariable Long userId, @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        ResponseObject responseObject = userService.getUserByUserId(userId, locale);
        if (responseObject.getStatus().equals("failed"))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseObject);

        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @DeleteMapping("/deleteUserById/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseObject> deleteUserById(@PathVariable Long userId, @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        ResponseObject responseObject = userService.deleteUserByUserId(userId, locale);
        if (responseObject.getStatus().equals("failed"))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseObject);

        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @PutMapping("/updateUserById/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<ResponseObject> updateUserById(@PathVariable Long userId, @RequestBody User user,@RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        ResponseObject responseObject = userService.updateUserByUserId(user, userId, locale);
        if (responseObject.getStatus().equals("failed"))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseObject);

        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

}

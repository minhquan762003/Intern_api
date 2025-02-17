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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Intern_api.model.ResponseObject;
import com.example.Intern_api.model.UserAndRole;
import com.example.Intern_api.service.UserAndRoleService;

@RestController
@RequestMapping("/at/api/userAndRole")
public class UserAndRoleController {
    @Autowired
    private UserAndRoleService userAndRoleService;


    @PostMapping("/saveUserAndRole")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseObject> saveUserAndRole(@RequestBody UserAndRole userAndRole,@RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        ResponseObject responseObject = userAndRoleService.saveUserAndRoleService(userAndRole, locale);
        if (responseObject.getStatus().equals("failed"))
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(responseObject);

        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    //Lấy tất cả role
    @GetMapping("/allUserAndRoles")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseObject> getAllUsers(@RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        ResponseObject responseObject = userAndRoleService.getAllUserAndRoles(locale);
        if (responseObject.getStatus().equals("failed"))
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(responseObject);

        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @GetMapping("/getUserAndRoleById/{Id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseObject> getUserAndRoleById(@PathVariable Long Id, @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        ResponseObject responseObject = userAndRoleService.getUserAndRoleById(Id, locale);
        if (responseObject.getStatus().equals("failed"))
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(responseObject);

        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @DeleteMapping("/deleteUserAndRoleById/{Id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseObject> deleteUserAndRoleById(@PathVariable Long Id, @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        ResponseObject responseObject = userAndRoleService.deleteUserAndRoleById(Id, locale);
        if (responseObject.getStatus().equals("failed"))
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(responseObject);

        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }
    
}

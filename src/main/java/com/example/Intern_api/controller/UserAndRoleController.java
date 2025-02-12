package com.example.Intern_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<ResponseObject> saveUserAndRole(@RequestBody UserAndRole userAndRole) {
        ResponseObject responseObject = userAndRoleService.saveUserAndRoleService(userAndRole);
        if (responseObject.getStatus().equals("failed"))
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(responseObject);

        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @GetMapping("/allUserAndRoles")
    public ResponseEntity<ResponseObject> getAllUsers() {
        ResponseObject responseObject = userAndRoleService.getAllUserAndRoles();
        if (responseObject.getStatus().equals("failed"))
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(responseObject);

        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @GetMapping("/getUserAndRoleById/{Id}")
    public ResponseEntity<ResponseObject> getUserAndRoleById(@PathVariable Long Id) {
        ResponseObject responseObject = userAndRoleService.getUserAndRoleById(Id);
        if (responseObject.getStatus().equals("failed"))
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(responseObject);

        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @DeleteMapping("/deleteUserAndRoleById/{Id}")
    public ResponseEntity<ResponseObject> deleteUserAndRoleById(@PathVariable Long Id) {
        ResponseObject responseObject = userAndRoleService.deleteUserAndRoleById(Id);
        if (responseObject.getStatus().equals("failed"))
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(responseObject);

        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }
    
}

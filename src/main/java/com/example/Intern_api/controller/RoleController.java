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
import org.springframework.web.bind.annotation.RestController;

import com.example.Intern_api.model.ResponseObject;
import com.example.Intern_api.model.Role;
import com.example.Intern_api.service.RoleService;

@RestController
@RequestMapping("/at/api/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping("/saveRole")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseObject> saveRole(@RequestBody Role role, @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        ResponseObject responseObject = roleService.saveRole(role, locale);
        if (responseObject.getStatus().equals("failed"))
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(responseObject);

        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @GetMapping("/allRoles")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseObject> getAllRoles(@RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        ResponseObject responseObject = roleService.getAllRoles(locale);
        if (responseObject.getStatus().equals("failed"))
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(responseObject);

        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @GetMapping("/getRoleById/{roleId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseObject> getRoleById(@PathVariable Long roleId, @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        ResponseObject responseObject = roleService.getRoleByRoleId(roleId, locale);
        if (responseObject.getStatus().equals("failed"))
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(responseObject);

        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @DeleteMapping("/deleteRoleById/{roleId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseObject> deleteRoleById(@PathVariable Long roleId, @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        ResponseObject responseObject = roleService.deleteRoleByRoleId(roleId, locale);
        if (responseObject.getStatus().equals("failed"))
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(responseObject);

        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @PutMapping("/updateRoleById/{roleId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseObject> updateRoleById(@RequestBody Role newRole, @PathVariable Long roleId, @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        ResponseObject responseObject = roleService.updateRoleByRoleId(newRole, roleId,locale);
        if (responseObject.getStatus().equals("failed"))
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(responseObject);

        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    
}

package com.example.Intern_api.service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.example.Intern_api.model.ResponseObject;
import com.example.Intern_api.model.UserAndRole;
import com.example.Intern_api.repository.UserAndRoleRepository;

@Service
public class UserAndRoleService {
    @Autowired
    private UserAndRoleRepository userAndRoleRepository;
    @Autowired
    private MessageSource messageSource;

    public ResponseObject saveUserAndRoleService (UserAndRole userAndRole,Locale locale){
        try {
            UserAndRole savedUserAndRole = userAndRoleRepository.save(userAndRole);
            String successMessage = messageSource.getMessage("userandrole.fetch.save.success",null, locale);
            return new ResponseObject("ok", successMessage, savedUserAndRole);
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("userandrole.fetch.save.failed",null, locale);
            return new ResponseObject("failed", errorMessage, "");
        }
    }

    public ResponseObject getAllUserAndRoles(Locale locale){
        try {
            List<UserAndRole> userAndRoles = userAndRoleRepository.findAll();
            String successMessage = messageSource.getMessage("userandrole.fetch.get.success",null, locale);
            return new ResponseObject("ok", successMessage, userAndRoles);
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("userandrole.fetch.get.failed",null, locale);
            return new ResponseObject("failed", errorMessage, "");
        }
    }
    public ResponseObject getUserAndRoleById(Long Id, Locale locale){
        Optional<UserAndRole> foundUserAndRole = userAndRoleRepository.findById(Id);
        if(foundUserAndRole.isPresent()){
            userAndRoleRepository.findById(Id);
            String successMessage = messageSource.getMessage("userandrole.fetch.get.success",null, locale);
            return new ResponseObject("ok", successMessage, foundUserAndRole);

        }
        String errorMessage = messageSource.getMessage("userandrole.fetch.get.failed",null, locale);

        return new ResponseObject("failed", errorMessage, "");

    }

    public ResponseObject deleteUserAndRoleById(Long Id, Locale locale){
        Optional<UserAndRole> foundUserAndRole = userAndRoleRepository.findById(Id);
        if(foundUserAndRole.isPresent()){
            userAndRoleRepository.deleteById(Id);
            String successMessage = messageSource.getMessage("userandrole.fetch.delete.success",null, locale);
            return new ResponseObject("ok", successMessage, foundUserAndRole);

        }
        String errorMessage = messageSource.getMessage("userandrole.fetch.delete.failed",null, locale);
        return new ResponseObject("failed", errorMessage, "");
    }

    // public ResponseObject updateUserAndRoleById(UserAndRole userAndRole, Long Id){

    // }
}

package com.example.Intern_api.service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.example.Intern_api.model.ResponseObject;
import com.example.Intern_api.model.Role;
import com.example.Intern_api.repository.RoleRepository;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MessageSource messageSource;

    public ResponseObject saveRole(Role role, Locale locale) {
        try {
            Role savedRole = roleRepository.save(role);
            String successMessage = messageSource.getMessage("role.fetch.save.success", null, locale);
            return new ResponseObject("ok", successMessage, savedRole);
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("role.fetch.save.failed", null, locale);
            return new ResponseObject("failed", errorMessage, "");

        }
    }

    public ResponseObject getAllRoles(Locale locale) {
        try {
            List<Role> foundLists = roleRepository.findAll();
            String successMessage = messageSource.getMessage("role.fetch.get.success", null, locale);
            return new ResponseObject("ok", successMessage, foundLists);
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("role.fetch.get.failed", null, locale);
            return new ResponseObject("failed", errorMessage, "");
        }
    }

    public ResponseObject getRoleByRoleId(Long roleId, Locale locale) {
        if ((roleRepository.findById(roleId)).isPresent()) {
            String successMessage = messageSource.getMessage("role.fetch.get.success", null, locale);
            return new ResponseObject("ok", successMessage, roleRepository.findById(roleId));
        } else {
            String errorMessage = messageSource.getMessage("role.fetch.get.failed", null, locale);
            return new ResponseObject("failed", errorMessage, "");
        }
    }

    public ResponseObject deleteRoleByRoleId(Long roleId, Locale locale) {
        if ((roleRepository.findById(roleId)).isPresent()) {
            Optional<Role> deletedRole = roleRepository.findById(roleId);
            roleRepository.deleteById(roleId);
            String successMessage = messageSource.getMessage("role.fetch.delete.success", null, locale);
            return new ResponseObject("ok", successMessage, deletedRole);
        } else {
            String errorMessage = messageSource.getMessage("role.fetch.delete.failed", null, locale);
            return new ResponseObject("failed", errorMessage, "");
        }
    }

    public ResponseObject updateRoleByRoleId(Role newRole, Long roleId, Locale locale) {

        try {
            Role updatedRole = roleRepository.findById(roleId).map(
                    role -> {
                        role.setRoleName(newRole.getRoleName());
                        role.setDescription(newRole.getDescription());
                        return roleRepository.save(role);
                    }).orElseGet(() -> {
                        return roleRepository.save(newRole);
                    });
            String successMessage = messageSource.getMessage("role.fetch.delete.success", null, locale);

            return new ResponseObject("ok", successMessage, updatedRole);

        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("role.fetch.update.failed", null, locale);
            return new ResponseObject("ok", errorMessage, "");

        }
    }

}

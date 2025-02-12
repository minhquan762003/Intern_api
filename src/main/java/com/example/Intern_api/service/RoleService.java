package com.example.Intern_api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Intern_api.model.ResponseObject;
import com.example.Intern_api.model.Role;
import com.example.Intern_api.repository.RoleRepository;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public ResponseObject saveRole(Role role) {
        try {
            Role savedRole = roleRepository.save(role);
            return new ResponseObject("ok", "Lưu role thành công", savedRole);
        } catch (Exception e) {
            return new ResponseObject("failed", "Lưu role không thành công", "");

        }
    }

    public ResponseObject getAllRoles() {
        try {
            List<Role> foundLists = roleRepository.findAll();
            return new ResponseObject("ok", "Truy xuất all role thành công", foundLists);
        } catch (Exception e) {
            return new ResponseObject("failed", "Truy xuất all role not thành công", "");
        }
    }

    public ResponseObject getRoleByRoleId(Long roleId) {
        if ((roleRepository.findById(roleId)).isPresent()) {
            return new ResponseObject("ok", "Truy xuất thành công", roleRepository.findById(roleId));
        } else {
            return new ResponseObject("failed", "Truy xuất không thành công", "");
        }
    }

    public ResponseObject deleteRoleByRoleId(Long roleId) {
        if ((roleRepository.findById(roleId)).isPresent()) {
            Optional<Role> deletedRole = roleRepository.findById(roleId);
            roleRepository.deleteById(roleId);
            return new ResponseObject("ok", "Xóa role thành công", deletedRole);
        } else {
            return new ResponseObject("failed", "Xóa role khôg thàng công", "");
        }
    }

    public ResponseObject updateRoleByRoleId(Role newRole, Long roleId) {

        try {
            Role updatedRole = roleRepository.findById(roleId).map(
                role -> {
                    role.setRoleName(newRole.getRoleName());
                    role.setDescription(newRole.getDescription());
                    return roleRepository.save(role);
                }).orElseGet(() -> {
                    return roleRepository.save(newRole);
                });
                return new ResponseObject("ok", "Sua thong tin san pham thanh cong", updatedRole);

        } catch (Exception e) {
            return new ResponseObject("ok", "Sua thong tin khong thanh cong", "");

        }
    }

}

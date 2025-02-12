package com.example.Intern_api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Intern_api.model.ResponseObject;
import com.example.Intern_api.model.UserAndRole;
import com.example.Intern_api.repository.UserAndRoleRepository;

@Service
public class UserAndRoleService {
    @Autowired
    private UserAndRoleRepository userAndRoleRepository;

    public ResponseObject saveUserAndRoleService (UserAndRole userAndRole){
        try {
            UserAndRole savedUserAndRole = userAndRoleRepository.save(userAndRole);
            return new ResponseObject("ok", "Lưu khóa ngoại user and role thành công", savedUserAndRole);
        } catch (Exception e) {
            return new ResponseObject("failed", "Lưu khóa ngoại user and role not thành công", "");
        }
    }

    public ResponseObject getAllUserAndRoles(){
        try {
            List<UserAndRole> userAndRoles = userAndRoleRepository.findAll();
            return new ResponseObject("ok", "Truy xuat khoa ngoai user va role thanh cong", userAndRoles);
        } catch (Exception e) {
            return new ResponseObject("failed", "Truy xuat khoa user va role that bai", "");
        }
    }
    public ResponseObject getUserAndRoleById(Long Id){
        Optional<UserAndRole> foundUserAndRole = userAndRoleRepository.findById(Id);
        if(foundUserAndRole.isPresent()){
            userAndRoleRepository.findById(Id);
            return new ResponseObject("ok", "Truy xuat khoa ngoai thanh cong", foundUserAndRole);

        }
        return new ResponseObject("failed", "Truy xuat khoa ngoai khong thanh cong", "");

    }

    public ResponseObject deleteUserAndRoleById(Long Id){
        Optional<UserAndRole> foundUserAndRole = userAndRoleRepository.findById(Id);
        if(foundUserAndRole.isPresent()){
            userAndRoleRepository.deleteById(Id);
            return new ResponseObject("ok", "Xoa khoa ngoai thanh cong", foundUserAndRole);

        }
        return new ResponseObject("failed", "Id khoa ngoai khong ton tai", "");
    }

    // public ResponseObject updateUserAndRoleById(UserAndRole userAndRole, Long Id){

    // }
}

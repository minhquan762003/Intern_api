package com.example.Intern_api.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Intern_api.auth.JwtUtils;
import com.example.Intern_api.model.ResponseObject;
import com.example.Intern_api.model.User;
import com.example.Intern_api.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    public ResponseObject registerUser(User user) {
        Optional<User> foundUser = userRepository.findByUserName(user.getUserName());
        Optional<User> foundEmail = userRepository.findByEmail(user.getEmail());
        if (foundUser.isPresent() || foundEmail.isPresent()) {
            return new ResponseObject("failed", "Đăng ký thất bại, tài khoản hoặc email đã tồn tại", "");
        }
        User savedUser = userRepository.save(user);
        return new ResponseObject("ok", "Đăng ký thành công", savedUser);
    }
    public ResponseObject loginUser(String userName, String password){
        Optional<User> foundUser = userRepository.findByUserName(userName);

        if(foundUser.isPresent() && password.equals(foundUser.get().getPassword())){
            
            User user = foundUser.get();
            
            Set<String> roles = user.getUserAndRoles()
                    .stream()
                    .map(userRole -> userRole.getRole().getRoleName())
                    .collect(Collectors.toSet());

            String token = jwtUtils.generateToken(foundUser.get().getUserId(), userName, roles);

            return new ResponseObject("ok", "Login thanh cong", token);

        }
        return new ResponseObject("failed", "Login that bai", "");
    }

    public ResponseObject getAllUsers() {
        // try {
            List<User> foundUsers = userRepository.findAll();
            return new ResponseObject("ok", "Truy xuất all user thành công", foundUsers);
        // } catch (Exception e) {
        //     return new ResponseObject("failed", "Truy xuất all user not thành công", "");
        // }
    }

    public ResponseObject getUserByUserId(Long userId) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isPresent()) {
            return new ResponseObject("ok", "Truy xuat user thanh cong", foundUser);
        }
        return new ResponseObject("failed", "Truy xuat user that bai", "");
    }

    public ResponseObject deleteUserByUserId(Long userId) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isPresent()) {
            userRepository.deleteById(userId);
            return new ResponseObject("ok", "Xoa user thanh cong", foundUser);
        }
        return new ResponseObject("failed", "User khong ton tai", "");
    }

    public ResponseObject updateUserByUserId(User newUser,Long userId){
        try {
            User updatedUser = userRepository.findById(userId).map(
                user->{
                    user.setUserName(newUser.getUserName());
                    user.setPassword(newUser.getPassword());
                    user.setEmail(newUser.getEmail());
                    user.setStatus(newUser.getStatus());
                    return userRepository.save(user);
                }).orElseGet(() -> {
                    return userRepository.save(newUser);
            });
            return new ResponseObject("ok", "Sua thong tin user thanh cong", updatedUser);
        } catch (Exception e) {
            return new ResponseObject("failed", "Sua thong tin khong thanh cong", "");
        }
    }
}

package com.example.Intern_api.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Intern_api.auth.JwtUtils;
import com.example.Intern_api.model.ResponseObject;
import com.example.Intern_api.model.Role;
import com.example.Intern_api.model.User;
import com.example.Intern_api.model.UserAndRole;
import com.example.Intern_api.repository.RoleRepository;
import com.example.Intern_api.repository.UserAndRoleRepository;
import com.example.Intern_api.repository.UserRepository;
import org.springframework.util.StringUtils;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAndRoleRepository userAndRoleRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    UserAndRoleService userAndRoleService;

    @Autowired
    RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseObject registerUser(User user) {

        if (!StringUtils.hasText(user.getUserName()) || !StringUtils.hasText(user.getEmail())
                || !StringUtils.hasText(user.getPassword())) {
            return new ResponseObject("failed", "Đăng ký thất bại, không được để trống thông tin", "");
        }

        String gmailRegex = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";
        Pattern pattern = Pattern.compile(gmailRegex);
        if (!pattern.matcher(user.getEmail()).matches()) {
            return new ResponseObject("failed", "Đăng ký thất bại, email phải là địa chỉ Gmail hợp lệ", "");
        }

        Optional<User> foundUser = userRepository.findByUserName(user.getUserName());
        Optional<User> foundEmail = userRepository.findByEmail(user.getEmail());
        if (foundUser.isPresent() || foundEmail.isPresent()) {
            return new ResponseObject("failed", "Đăng ký thất bại, tài khoản hoặc email đã tồn tại", "");
        }
        Role userRole = roleRepository.findByRoleName("USER");

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);
        UserAndRole userAndRole = new UserAndRole();

        userAndRole.setRole(userRole);
        userAndRole.setUser(savedUser);
        userAndRoleService.saveUserAndRoleService(userAndRole);

        return new ResponseObject("ok", "Đăng ký thành công", savedUser);
    }

    public ResponseObject loginUser(String userName, String password) {
        Optional<User> foundUser = userRepository.findByUserName(userName);

        if (foundUser.isPresent()) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            // Kiểm tra mật khẩu đã nhập có khớp với mật khẩu đã mã hóa trong DB
            if (passwordEncoder.matches(password, foundUser.get().getPassword())) {
                List<Role> roleList = userAndRoleRepository.findRolesByUserId(foundUser.get().getUserId());
                Set<String> roles = roleList.stream().map(Role::getRoleName).collect(Collectors.toSet());

                System.out.println("Danh sách roles của user: " + roles);
                String token = jwtUtils.generateToken(foundUser.get().getUserId(), userName, roles);

                return new ResponseObject("ok", "Login thành công", token);
            }
        }
        return new ResponseObject("failed", "Login thất bại", "");
    }

    public ResponseObject getAllUsers(int page, int size) {
        try {
        Pageable pageable = PageRequest.of(page, size);

        Page<User> foundUsers = userRepository.findAll(pageable);
        return new ResponseObject("ok", "Truy xuất all user thành công", foundUsers);
        } catch (Exception e) {
        return new ResponseObject("failed", "Truy xuất all user not thành công", "");
        }
    }

    public ResponseObject getUserByUserId(Long userId) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isPresent()) {
            return new ResponseObject("ok", "   Truy xuat user thanh cong", foundUser);
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

    public ResponseObject updateUserByUserId(User newUser, Long userId) {
        try {
            // Kiểm tra dữ liệu đầu vào
            if (newUser.getUserName() == null || newUser.getUserName().isBlank()) {
                return new ResponseObject("failed", "Tên người dùng không được để trống", "");
            }
            if (newUser.getPassword() == null || newUser.getPassword().length() < 6) {
                return new ResponseObject("failed", "Mật khẩu phải có ít nhất 6 ký tự", "");
            }
            String gmailRegex = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";
            Pattern pattern = Pattern.compile(gmailRegex);
            if (newUser.getEmail() == null ||!pattern.matcher(newUser.getEmail()).matches() ) {
                return new ResponseObject("failed", "Email không hợp lệ", "");
            }

            // Tìm user theo ID
            User updatedUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

            // Cập nhật thông tin
            updatedUser.setUserName(newUser.getUserName());
            updatedUser.setPassword(passwordEncoder.encode(newUser.getPassword())); // Mã hóa mật khẩu
            updatedUser.setEmail(newUser.getEmail());
            updatedUser.setStatus(newUser.getStatus());

            // Lưu vào database
            userRepository.save(updatedUser);

            return new ResponseObject("ok", "Sửa thông tin user thành công", updatedUser);
        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi ra console để debug
            return new ResponseObject("failed", "Lỗi khi cập nhật user: " + e.getMessage(), "");
        }
    }
}

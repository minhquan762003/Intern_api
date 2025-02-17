package com.example.Intern_api.service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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

    @Autowired
    private MessageSource messageSource;


    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseObject registerUser(User user, Locale locale) {

        if (!StringUtils.hasText(user.getUserName()) || !StringUtils.hasText(user.getEmail())
                || !StringUtils.hasText(user.getPassword())) {
            String errorMessage = messageSource.getMessage("user.fetch.regiter.failed.empty", null, locale);
            return new ResponseObject("failed", errorMessage, "");
        }

        String gmailRegex = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";
        Pattern pattern = Pattern.compile(gmailRegex);
        if (!pattern.matcher(user.getEmail()).matches()) {
            String errorMessage = messageSource.getMessage("user.fetch.regiter.failed.gmail", null, locale);
            return new ResponseObject("failed", errorMessage, "");
        }

        Optional<User> foundUser = userRepository.findByUserName(user.getUserName());
        Optional<User> foundEmail = userRepository.findByEmail(user.getEmail());
        if (foundUser.isPresent() || foundEmail.isPresent()) {
            String errorMessage = messageSource.getMessage("user.failed.regiter.failed.alreadyused", null, locale);
            return new ResponseObject("failed", errorMessage, "");
        }
        Role userRole = roleRepository.findByRoleName("USER");

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);
        UserAndRole userAndRole = new UserAndRole();

        userAndRole.setRole(userRole);
        userAndRole.setUser(savedUser);
        userAndRoleService.saveUserAndRoleService(userAndRole, locale);

        String successMessage = messageSource.getMessage("user.fetch.regiter.success",null, locale);
        return new ResponseObject("ok", successMessage, savedUser);
    }

    public ResponseObject loginUser(String userName, String password, Locale locale) {
        Optional<User> foundUser = userRepository.findByUserName(userName);

        if (foundUser.isPresent()) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            // Kiểm tra mật khẩu đã nhập có khớp với mật khẩu đã mã hóa trong DB
            if (passwordEncoder.matches(password, foundUser.get().getPassword())) {
                List<Role> roleList = userAndRoleRepository.findRolesByUserId(foundUser.get().getUserId());
                Set<String> roles = roleList.stream().map(Role::getRoleName).collect(Collectors.toSet());

                // System.out.println("Danh sách roles của user: " + roles);
                String token = jwtUtils.generateToken(foundUser.get().getUserId(), userName, roles);
                String successMessage = messageSource.getMessage("user.fetch.login.success", null, locale);
                return new ResponseObject("ok",successMessage , token);
            }
        }
        String errorMessage = messageSource.getMessage("user.fetch.login.failed",null,  locale);
        return new ResponseObject("failed",errorMessage , "");
    }

    public ResponseObject getAllUsers(int page, int size, Locale locale) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<User> foundUsers = userRepository.findAll(pageable);

            String successMessage = messageSource.getMessage("user.fetch.success", null, locale);

            return new ResponseObject("ok", successMessage , foundUsers);
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("user.fetch.failed",null,  locale);
            return new ResponseObject("failed",errorMessage , "");
        }
    }

    public ResponseObject getUserByUserId(Long userId, Locale locale) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isPresent()) {
            String successMessage = messageSource.getMessage("user.fetch.one.success",null, locale);
            return new ResponseObject("ok", successMessage, foundUser);
        }

        String errorMessage = messageSource.getMessage("user.fetch.one.failed",null, locale);
        return new ResponseObject("failed", errorMessage , "");
    }

    public ResponseObject deleteUserByUserId(Long userId, Locale locale) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isPresent()) {
            userRepository.deleteById(userId);
            String successMessage = messageSource.getMessage("user.fetch.delete.success",null, locale);
            return new ResponseObject("ok", successMessage, foundUser);
        }
        String errorMessage = messageSource.getMessage("user.fetch.delete.failed",null, locale);
        return new ResponseObject("failed", errorMessage, "");
    }

    public ResponseObject updateUserByUserId(User newUser, Long userId, Locale locale) {
        try {
            // Kiểm tra dữ liệu đầu vào
            if (newUser.getUserName() == null || newUser.getUserName().isBlank()) {
                String errorMessage = messageSource.getMessage("user.fetch.update.username",null, locale);
                return new ResponseObject("failed", errorMessage, "");
            }
            if (newUser.getPassword() == null || newUser.getPassword().length() < 6) {
                String errorMessage = messageSource.getMessage("user.fetch.update.password",null, locale);
                return new ResponseObject("failed", errorMessage, "");
            }
            String gmailRegex = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";
            Pattern pattern = Pattern.compile(gmailRegex);
            if (newUser.getEmail() == null || !pattern.matcher(newUser.getEmail()).matches()) {
                String errorMessage = messageSource.getMessage("user.fetch.update.email",null, locale);
                return new ResponseObject("failed", errorMessage, "");
            }

            // Tìm user theo ID
            String errorMessage = messageSource.getMessage("user.fetch.update.notpresent",null, locale);
            User updatedUser = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException(errorMessage));

            // Cập nhật thông tin
            updatedUser.setUserName(newUser.getUserName());
            updatedUser.setPassword(passwordEncoder.encode(newUser.getPassword())); // Mã hóa mật khẩu
            updatedUser.setEmail(newUser.getEmail());
            updatedUser.setStatus(newUser.getStatus());

            // Lưu vào database
            userRepository.save(updatedUser);

            String successMessage = messageSource.getMessage("user.fetch.update.success",null, locale);
            return new ResponseObject("ok", successMessage, updatedUser);
        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi ra console để debug
            String successMessage = messageSource.getMessage("user.fetch.update.exception",null, locale);
            return new ResponseObject("failed",successMessage + " " + e.getMessage(), "");
        }
    }
}

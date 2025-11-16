package com.basis.anhangda37.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import com.basis.anhangda37.domain.User;
import com.basis.anhangda37.service.UploadService;
import com.basis.anhangda37.service.UserService;


import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserDashboardController {
    @Value("${admin.dashboard.size.user}")
    private int pageSizeOfUserDashboard;

    private final UserService userService;
    private final UploadService uploadService;
    private final PasswordEncoder passwordEncoder;

    public UserDashboardController(UserService userService, UploadService uploadService,
            PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.uploadService = uploadService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(value = "/admin/user")
    public String routeUserTable(Model model,
            @RequestParam(name = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, pageSizeOfUserDashboard);
        Page<User> puser = userService.getAllUsers(pageable);
        List<User> users = puser.getContent();
        model.addAttribute("users", users);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", puser.getTotalPages());
        return "admin/user/show";
    }

    @GetMapping("/admin/user/{id}")
    public String getDetailUserPage(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("id", id);
        return "admin/user/detail";
    }

    @GetMapping(value = "/admin/user/create")
    public String routeUserTableGet(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    @PostMapping(value = "/admin/user/create")
    public String routeUserTablePost(Model model,
            @ModelAttribute("newUser") @Valid User user,
            BindingResult newUserBindingResult,
            @RequestParam("hoidanitFile") MultipartFile file) {
        List<FieldError> errors = newUserBindingResult.getFieldErrors();
        errors.forEach(e -> {
            System.out.println(e.getField() + " " + e.getDefaultMessage());
        });
        if (newUserBindingResult.hasErrors()) {
            return "admin/user/create";
        }
        String avatarString = uploadService.handleSaveUploadFile(file, "avatar");
        user.setAvatar((avatarString == null || avatarString.isBlank()) ? null : avatarString);
        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        user.setRole(userService.getRoleByName(user.getRole().getName()));
        userService.saveUser(user);
        return "redirect:/admin/user";
    }

    @GetMapping(value = "/admin/user/update/{id}")
    public String getUpdatePage(Model model, @PathVariable Long id) {
        User user = userService.getUserById(id);
        model.addAttribute("newUser", user);
        model.addAttribute("avatarPreview", user.getAvatar());
        return "admin/user/update";
    }

    @PostMapping(value = "/admin/user/update")
    public String postUpdateUser(Model model,
            @ModelAttribute("newUser") User user,
            @RequestParam("hoidanitFile") MultipartFile file) {
        User user1 = userService.getUserById(user.getId());
        if (user1 != null) {
            user1.setAddress(user.getAddress());
            user1.setFullName(user.getFullName());
            user1.setPhone(user.getPhone());
            user1.setRole(user.getRole());
            String avatarPath = uploadService.handleSaveUploadFile(file, "avatar");
            if (!(avatarPath == null || avatarPath.isBlank() || avatarPath.isEmpty())) {
                user1.setAvatar(avatarPath);
            }
        }
        userService.saveUser(user1);
        return "redirect:/admin/user";
    }

    @GetMapping(value = "/admin/user/delete/{id}")
    public String getDeletePage(Model model, @PathVariable Long id) {
        model.addAttribute("id", id);
        User user = new User();
        user.setId(id);
        model.addAttribute("user", user);
        return "admin/user/delete";
    }

    @PostMapping(value = "/admin/user/delete")
    public String deleteUser(Model model, @ModelAttribute("user") User user) {
        userService.deleteUser(user.getId());
        return "redirect:/admin/user";
    }

}

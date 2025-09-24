package com.basis.anhangda37.controller.admin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.catalina.realm.UserDatabaseRealm;
import org.aspectj.internal.lang.annotation.ajcDeclareAnnotation;
import org.hibernate.loader.ast.internal.MultiKeyLoadChunker;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.basis.anhangda37.domain.User;
import com.basis.anhangda37.repository.UserRepository;
import com.basis.anhangda37.service.UploadService;
import com.basis.anhangda37.service.UserService;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import jakarta.servlet.ServletContext;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final UploadService uploadService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, UploadService uploadService, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.userService = userService;
        this.uploadService = uploadService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @GetMapping(value = "/admin/user")
    public String routeUserTable(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
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
            BindingResult result,
            @RequestParam("hoidanitFile") MultipartFile file) {
        List<FieldError> errors = result.getFieldErrors();
        errors.forEach(e -> {
            System.out.println(e.getObjectName() + " " + e.getDefaultMessage());
        });
        String avatarString = uploadService.handleSaveUploadFile(file, "avatar");
        user.setAvatar((avatarString == null || avatarString.isBlank()) ? null : avatarString);
        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
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
        if(user1 != null) {
            user1.setAddress(user.getAddress());
            user1.setFullName(user.getFullName());
            user1.setPhone(user.getPhone());
            user1.setRole(user.getRole());
            String avatarPath = uploadService.handleSaveUploadFile(file, "avatar");
            if(!(avatarPath == null || avatarPath.isBlank() || avatarPath.isEmpty())) {
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

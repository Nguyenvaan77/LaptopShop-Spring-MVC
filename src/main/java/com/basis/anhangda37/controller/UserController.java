package com.basis.anhangda37.controller;

import java.util.List;

import org.apache.catalina.realm.UserDatabaseRealm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.basis.anhangda37.domain.User;
import com.basis.anhangda37.repository.UserRepository;
import com.basis.anhangda37.service.UserService;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/admin/user", method = RequestMethod.GET)
    public String routeUserTable(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/user/userTable";
    }

    @GetMapping("/admin/user/{id}")
    public String getDetailUserPage(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("id", id);
        return "admin/user/show";
    }
    

    @RequestMapping(value = "/admin/user/create", method = RequestMethod.GET)
    public String routeUserTableGet(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    @RequestMapping(value = "/admin/user/create", method = RequestMethod.POST)
    public String routeUserTablePost(Model model, @ModelAttribute("newUser") User user) {
        userService.saveUser(user);
        return "redirect:/admin/user";
    }
    
    @GetMapping(value = "/admin/user/update/{id}")
    public String getUpdatePage(Model model, @PathVariable Long id) {
        User user = userService.getUserById(id);
        model.addAttribute("newUser", user);
        return "admin/user/update";
    }

    @PostMapping(value = "/admin/user/update")
    public String postUpdateUser(Model model, @ModelAttribute("newUser") User user) {
        User user1 = userService.getUserById(user.getId());
        if(user1 != null) {
            user1.setFullName(user.getFullName());
            user1.setPhoneNumber(user.getPhoneNumber());
            user1.setAddress(user.getAddress());
            userService.saveUser(user1);
        }
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

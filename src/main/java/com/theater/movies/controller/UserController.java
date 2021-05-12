package com.theater.movies.controller;

import com.theater.movies.enums.Status;
import com.theater.movies.model.CommonResponse;
import com.theater.movies.model.PageableRequest;
import com.theater.movies.model.User;
import com.theater.movies.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public CommonResponse getUsers(PageableRequest pageableRequest, HttpServletRequest request) {
        return userService.getUsers(pageableRequest, request);
    }

    @GetMapping("/user/{id}")
    public CommonResponse getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PostMapping("/register")
    public CommonResponse registerUser(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/user")
    public CommonResponse createUser(@RequestBody User user, HttpServletRequest request) {
        return userService.createUser(user, request);
    }

    @DeleteMapping("/user/{id}")
    public CommonResponse deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @PutMapping("/user/{id}")
    public CommonResponse updateUser(@PathVariable Long id, @RequestBody User user, HttpServletRequest request) {
        return userService.updateUser(id, user, request);
    }

    @PutMapping("/user/{id}/status")
    public CommonResponse updateUserStatus(@PathVariable Long id, @RequestParam("status") Integer status, HttpServletRequest request) {
        return userService.updateUserStatus(id, Status.fromInteger(status), request);
    }

    @DeleteMapping("/oauth/token")
    public CommonResponse revokeToken(HttpServletRequest request) {
        return userService.logout(request);
    }

    @PutMapping("/user/password")
    public CommonResponse changePassword(@RequestParam("old_password") String oldPassword,
                                         @RequestParam("new_password") String newPassword,
                                         HttpServletRequest request) {
        return userService.changePassword(oldPassword, newPassword, request);
    }
}

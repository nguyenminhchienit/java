package hutech.apicrud.controllers;

import hutech.apicrud.dto.request.ApiResponse;
import hutech.apicrud.dto.request.UserCreateRequest;
import hutech.apicrud.dto.request.UserUpdateRequest;
import hutech.apicrud.dto.response.UserResponse;
import hutech.apicrud.entities.User;
import hutech.apicrud.services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @PostMapping
    ApiResponse<User> addUser(@RequestBody @Valid UserCreateRequest request) {
        ApiResponse<User> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setData(userService.addUser(request));
        return response;
    }

    @GetMapping
    ApiResponse<User> getAllUsers() {
       var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("User name: {}", authentication.getName());

        authentication.getAuthorities().forEach(role -> log.info(role.getAuthority()));

        ApiResponse response = new ApiResponse<>();
        response.setSuccess(true);
        response.setData(userService.getAllUsers());
        response.setMessage("Get all users successfully");
        return response;
    }

    @GetMapping("my-info")
    ApiResponse<User> getUserMyInfoLogin(){
        ApiResponse<User> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setData(userService.getUserMyInfoLogin());
        return response;
    }

    @GetMapping("/{userId}")
    ApiResponse<User> getUser(@PathVariable("userId") String userId) {
        ApiResponse response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Successfully retrieved user");
        response.setData(userService.getUserById(userId));
        return response;
    }

    @PutMapping("/{userId}")
    ApiResponse<User> updateUser(@PathVariable("userId") String userId, @RequestBody @Valid UserUpdateRequest request) {
        ApiResponse response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Successfully updated user");
        response.setData(userService.updateUser(request,userId));
        return response;
    }

    @DeleteMapping("/{userId}")
    ApiResponse deleteUser(@PathVariable("userId") String userId) {
        userService.deleteUser(userId);
        ApiResponse response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Successfully deleted user");

        return response;
    }

}

package hutech.apicrud.controllers;

import hutech.apicrud.dto.request.ApiResponse;
import hutech.apicrud.dto.request.UserCreateRequest;
import hutech.apicrud.dto.request.UserUpdateRequest;
import hutech.apicrud.dto.response.UserResponse;
import hutech.apicrud.entities.User;
import hutech.apicrud.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
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
    List<User> getAllUsers() {
        return userService.getAllUsers();
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

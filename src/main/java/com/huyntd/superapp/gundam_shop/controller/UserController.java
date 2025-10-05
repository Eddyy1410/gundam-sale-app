package com.huyntd.superapp.gundam_shop.controller;

import com.huyntd.superapp.gundam_shop.dto.ApiResponse;
import com.huyntd.superapp.gundam_shop.dto.request.UserRegisterRequest;
import com.huyntd.superapp.gundam_shop.dto.request.UserUpdateRequest;
import com.huyntd.superapp.gundam_shop.dto.response.UserResponse;
import com.huyntd.superapp.gundam_shop.model.User;
import com.huyntd.superapp.gundam_shop.service.User.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/user")
public class UserController {

    UserService userService;

    @PostMapping("/")
    ApiResponse<UserResponse> addUser(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.create(userRegisterRequest))
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getCustomer(@PathVariable String userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getCustomer(userId))
                .build();
    }

    @GetMapping("/")
    List<User> getAllUsers() {
        return userService.getUser();
    }

    @PutMapping("/{userId}")
    ApiResponse<User> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest userUpdateRequest) {
        return ApiResponse.<User>builder()
                .result(userService.updateUser(userId, userUpdateRequest))
                .build();
    }

}

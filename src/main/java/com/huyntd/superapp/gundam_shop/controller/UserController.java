package com.huyntd.superapp.gundam_shop.controller;

import com.huyntd.superapp.gundam_shop.dto.ApiResponse;
import com.huyntd.superapp.gundam_shop.dto.request.UserCreateRequest;
import com.huyntd.superapp.gundam_shop.dto.request.UserRegisterRequest;
import com.huyntd.superapp.gundam_shop.dto.request.UserUpdateRequest;
import com.huyntd.superapp.gundam_shop.dto.response.UserResponse;
import com.huyntd.superapp.gundam_shop.model.User;
import com.huyntd.superapp.gundam_shop.service.user.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/user")
public class UserController {

    UserService userService;

    @PostMapping("/register")
    ApiResponse<UserResponse> addCustomer(@RequestBody @Valid UserRegisterRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createCustomer(request))
                .message("User registered successfully!")
                .build();
    }

    @PostMapping("/create")
    ApiResponse<UserResponse> addUser(@RequestBody @Valid UserCreateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.create(request))
                .message("User created successfully!")
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable int userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }

    @GetMapping("/myInfo")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @GetMapping("/")
    ApiResponse<List<UserResponse>> getAllUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable int userId, @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, request))
                .build();
    }

}

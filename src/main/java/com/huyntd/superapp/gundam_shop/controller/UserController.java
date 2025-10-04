package com.huyntd.superapp.gundam_shop.controller;

import com.huyntd.superapp.gundam_shop.dto.request.UserRegisterRequest;
import com.huyntd.superapp.gundam_shop.service.User.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/user")
public class UserController {

    UserService userService;

    @PostMapping("/")
    public String addUser(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {
        userService.create(userRegisterRequest);
        return "User added";
    }

}

package com.huyntd.superapp.gundam_shop.service.user;

import com.huyntd.superapp.gundam_shop.dto.request.UserCreateRequest;
import com.huyntd.superapp.gundam_shop.dto.request.UserOAuth2RegisterRequest;
import com.huyntd.superapp.gundam_shop.dto.request.UserRegisterRequest;
import com.huyntd.superapp.gundam_shop.dto.request.UserUpdateRequest;
import com.huyntd.superapp.gundam_shop.dto.response.UserResponse;
import com.huyntd.superapp.gundam_shop.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserResponse createCustomer(UserRegisterRequest request);
    UserResponse create(UserCreateRequest request);
    UserResponse getUser(int userId);
    UserResponse getMyInfo();
    UserResponse updateUser(int userId, UserUpdateRequest request);
    List<UserResponse> getUsers();
    Optional<User> createOAuth2(UserOAuth2RegisterRequest request);
}

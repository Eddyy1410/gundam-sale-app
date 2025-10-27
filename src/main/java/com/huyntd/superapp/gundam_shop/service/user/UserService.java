package com.huyntd.superapp.gundam_shop.service.user;

import java.util.List;
import java.util.Optional;

import com.huyntd.superapp.gundam_shop.dto.request.UserCreateRequest;
import com.huyntd.superapp.gundam_shop.dto.request.UserOAuth2RegisterRequest;
import com.huyntd.superapp.gundam_shop.dto.request.UserProfileUpdateRequest;
import com.huyntd.superapp.gundam_shop.dto.request.UserRegisterRequest;
import com.huyntd.superapp.gundam_shop.dto.request.UserUpdateRequest;
import com.huyntd.superapp.gundam_shop.dto.response.UserResponse;
import com.huyntd.superapp.gundam_shop.model.User;

public interface UserService {
    UserResponse createCustomer(UserRegisterRequest request);
    UserResponse create(UserCreateRequest request);
    UserResponse getUser(int userId);
    UserResponse getMyInfo();
    UserResponse updateUser(int userId, UserUpdateRequest request);
    List<UserResponse> getUsers();
    Optional<User> createOAuth2(UserOAuth2RegisterRequest request);
    void changePassword(String currentPassword, String newPassword);
    UserResponse updateMyProfile(UserProfileUpdateRequest request);
    void saveFcmToken(String fcmToken);
}

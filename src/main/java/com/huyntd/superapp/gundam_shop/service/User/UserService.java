package com.huyntd.superapp.gundam_shop.service.User;

import com.huyntd.superapp.gundam_shop.dto.request.UserRegisterRequest;
import com.huyntd.superapp.gundam_shop.dto.request.UserUpdateRequest;
import com.huyntd.superapp.gundam_shop.dto.response.UserResponse;
import com.huyntd.superapp.gundam_shop.model.User;

import java.util.List;

public interface UserService {
    UserResponse create(UserRegisterRequest request);
    UserResponse getCustomer(String userId);
    UserResponse updateUser(String userId, UserUpdateRequest request);
    List<User> getUser();
}

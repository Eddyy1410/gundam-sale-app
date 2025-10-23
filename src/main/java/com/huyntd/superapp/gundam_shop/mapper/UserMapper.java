package com.huyntd.superapp.gundam_shop.mapper;

import com.huyntd.superapp.gundam_shop.dto.request.UserCreateRequest;
import com.huyntd.superapp.gundam_shop.dto.request.UserRegisterRequest;
import com.huyntd.superapp.gundam_shop.dto.request.UserUpdateRequest;
import com.huyntd.superapp.gundam_shop.dto.response.UserResponse;
import com.huyntd.superapp.gundam_shop.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring") // componentModel= spring ???
public interface UserMapper {
    @Mapping(source = "password", target = "passwordHash")
    User toUser(UserRegisterRequest userRegisterRequest);

    @Mapping(source = "password", target = "passwordHash")
    User toUser(UserCreateRequest userCreateRequest);

    UserResponse toUserResponse(User user);

    @Mapping(source = "password", target = "passwordHash")
    void updateUser(@MappingTarget User user, UserUpdateRequest userUpdateRequest);
}

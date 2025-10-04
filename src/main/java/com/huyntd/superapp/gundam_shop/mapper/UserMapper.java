package com.huyntd.superapp.gundam_shop.mapper;

import com.huyntd.superapp.gundam_shop.dto.request.UserRegisterRequest;
import com.huyntd.superapp.gundam_shop.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") // componentModel= spring ???
public interface UserMapper {
    User toUser(UserRegisterRequest userRegisterRequest);
}

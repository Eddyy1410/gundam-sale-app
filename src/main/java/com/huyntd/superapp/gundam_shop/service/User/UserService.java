package com.huyntd.superapp.gundam_shop.service.User;

import com.huyntd.superapp.gundam_shop.dto.request.UserRegisterRequest;
import com.huyntd.superapp.gundam_shop.model.User;

public interface UserService {
    User create(UserRegisterRequest request);
}

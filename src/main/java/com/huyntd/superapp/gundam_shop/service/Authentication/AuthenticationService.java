package com.huyntd.superapp.gundam_shop.service.Authentication;

import com.huyntd.superapp.gundam_shop.dto.request.AuthenticationRequest;

public interface AuthenticationService {
    boolean authenticate(AuthenticationRequest request);
}

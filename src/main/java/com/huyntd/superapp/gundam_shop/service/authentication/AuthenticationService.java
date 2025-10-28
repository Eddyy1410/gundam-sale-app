package com.huyntd.superapp.gundam_shop.service.authentication;

import com.huyntd.superapp.gundam_shop.dto.request.AuthenticationRequest;
import com.huyntd.superapp.gundam_shop.dto.request.GoogleTokenRequest;
import com.huyntd.superapp.gundam_shop.dto.request.IntrospectRequest;
import com.huyntd.superapp.gundam_shop.dto.request.LogoutRequest;
import com.huyntd.superapp.gundam_shop.dto.response.AuthenticationResponse;
import com.huyntd.superapp.gundam_shop.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;
    AuthenticationResponse processGoogleToken(GoogleTokenRequest request) throws GeneralSecurityException, IOException;
    Void logout(LogoutRequest request) throws JOSEException, ParseException;
    Authentication getAuthentication(String token);
}

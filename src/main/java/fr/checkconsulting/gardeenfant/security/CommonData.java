package fr.checkconsulting.gardeenfant.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class CommonData {

    public static String getEmail() {
        Jwt user = ((Jwt) SecurityContextHolder.getContext().getAuthentication().getCredentials());
        return String.valueOf(user.getClaims().get("email"));
    }
}

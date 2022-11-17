package fr.checkconsulting.gardeenfant.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class CommonData {
    static Jwt user =  ((Jwt) SecurityContextHolder.getContext().getAuthentication().getCredentials());

    public static String getEmail(){
        return String.valueOf(user.getClaims().get("email"));
    }
}

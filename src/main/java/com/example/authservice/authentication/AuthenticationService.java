package com.example.authservice.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.authservice.appuser.AppUser;
import com.example.authservice.appuser.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AppUserRepository appUserRepository;
    private final static String USER_NOT_AUTHORIZED = "users with role %s are forbidden";
    private final static String USER_NOT_AUTHENTICATED = "user is not authenticated";
    public UserInfo getUserInfoFromToken(String token, String role) throws Exception {
        try{
            String actualToken = token.substring("Bearer ".length());
            Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(actualToken);
            String username = decodedJWT.getSubject();
            List<String> roles = Arrays.asList(decodedJWT.getClaim("roles").asArray(String.class));
            Optional<AppUser> optionalUser = appUserRepository
                    .findByEmail(username);

            if(optionalUser.isEmpty() || !optionalUser.get().isEnabled()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, USER_NOT_AUTHENTICATED);
            }
            if(role != null && !role.isEmpty() && !roles.contains(role)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, String.format(USER_NOT_AUTHORIZED, role));
            }
            return new UserInfo(username, roles);
        }
        catch(Exception e){
            if(!(e instanceof ResponseStatusException))
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
            throw e;
        }
    }

}

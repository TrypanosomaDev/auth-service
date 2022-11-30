package com.example.authservice.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @GetMapping
    public UserInfo check(@RequestHeader("Authorization") String token, @RequestParam("role") String role)
            throws Exception {
        return authenticationService.getUserInfoFromToken(token, role);
    }

}


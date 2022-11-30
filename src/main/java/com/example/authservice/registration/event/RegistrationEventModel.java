package com.example.authservice.registration.event;

import com.example.authservice.appuser.AppUser;
import lombok.Data;

@Data
public class RegistrationEventModel {
    private String firstName;
    private String lastName;
    private String username;
    private Long userId;

    public RegistrationEventModel(AppUser appUser) {
        this.firstName = appUser.getFirstName();
        this.lastName = appUser.getLastName();
        this.username = appUser.getUsername();
        this.userId = appUser.getId();
    }
}


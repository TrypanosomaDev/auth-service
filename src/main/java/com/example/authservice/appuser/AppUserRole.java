package com.example.authservice.appuser;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum AppUserRole {
    @JsonProperty("user")
    USER,
    @JsonProperty("admin")
    ADMIN
}

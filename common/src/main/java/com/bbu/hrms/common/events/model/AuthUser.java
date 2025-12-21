package com.bbu.hrms.common.events.model;

import lombok.Data;
import java.util.Set;

@Data
public class AuthUser {

    private Long id;
    private String username;
    private String email;

    private Set<String> roles;
    private Set<String> permissions;
}

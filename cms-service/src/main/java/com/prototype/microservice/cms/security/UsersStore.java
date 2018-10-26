package com.prototype.microservice.cms.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class UsersStore {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${cms-service.credentials.users.admin.username:admin}")
    private String adminUsername;

    @Value("${cms-service.credentials.users.admin.password}")
    private String adminPassword;

    @Value("${cms-service.credentials.users.apiClient.username:apiClient}")
    private String apiClientUsername;

    @Value("${cms-service.credentials.users.apiClient.password}")
    private String apiClientPassword;

    private final GrantedAuthority roleAdmin = new SimpleGrantedAuthority("ROLE_ADMIN");
    private final GrantedAuthority roleActuator = new SimpleGrantedAuthority("ROLE_ACTUATOR");
    private final GrantedAuthority roleApiClient = new SimpleGrantedAuthority("ROLE_APICLIENT");
    private final Set<GrantedAuthority> authAdmin;
    private final Set<GrantedAuthority> authApiClient;

    public UsersStore() {
        authAdmin = new HashSet<>();
        authAdmin.add(roleAdmin);
        authAdmin.add(roleActuator);
        authApiClient = new HashSet<>();
        authApiClient.add(roleApiClient);
    }

    private User buildUser(final String username, final String password, final Set<GrantedAuthority> roles) {
        User user = new User(username, passwordEncoder.encode(password), roles);
        return user;
    }

    public Map<String, User> getUsers() {
        Map<String, User> users = new HashMap<>();
        users.put(adminUsername, buildUser(adminUsername, adminPassword, authAdmin));
        users.put(apiClientUsername, buildUser(apiClientUsername, apiClientPassword, authApiClient));
        return users;
    }
}

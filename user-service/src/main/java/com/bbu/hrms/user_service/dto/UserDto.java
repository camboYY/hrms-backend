package com.bbu.hrms.user_service.dto;


import java.util.Set;


public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String status;
    private Set<String> roles; // role names
    private Long authUserId;


    public UserDto() {

    }
    public UserDto( Long id,  String username,  String email, String firstName, String lastName, String phoneNumber, String status, Set<String> roles, Long authUserId) {
        this.firstName = firstName;
        this.id = id;
        this.username = username;
        this.email = email;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.roles = roles;
        this.authUserId = authUserId;
    }

    public Long getAuthUserId() {
        return authUserId;
    }

    public void setAuthUserId(Long authUserId) {
        this.authUserId = authUserId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

}


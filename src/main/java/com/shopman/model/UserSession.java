package com.shopman.model;

import java.io.Serializable;

public class UserSession implements Serializable {
    private final Long memberId;
    private final String username;
    private final String fullName;
    private final String role;
    private final String shippingAddress;

    public UserSession(Long memberId, String username, String fullName, String role, String shippingAddress) {
        this.memberId = memberId;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
        this.shippingAddress = shippingAddress;
    }

    public Long getMemberId() { return memberId; }
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public String getRole() { return role; }
    public String getShippingAddress() { return shippingAddress; }
}

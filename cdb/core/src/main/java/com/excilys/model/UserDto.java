package com.excilys.model;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public final class UserDto implements UserDetails {
    /** */
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    Long id;
    private UserRoles role;

    @Override
    public String getUsername() {
        return this.username;
    }

    public void setUsername(final String name) {
        this.username = name;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.role == null ? Arrays.asList() : Arrays.asList(this.role);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UserRoles getRole() {
        return this.role;
    }

    public void setRole(final UserRoles role) {
        this.role = role;
    }

}

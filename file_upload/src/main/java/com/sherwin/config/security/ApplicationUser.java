package com.sherwin.config.security;

import com.sherwin.features.uploadingfiles.dto.User;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ApplicationUser implements UserDetails {

    private final Set<? extends GrantedAuthority> grantedAuthorities ;
    private final String username;
    private final String password;
    private final boolean isAccountNonExpired ;
    private final boolean isAccountNonLocked ;
    private final boolean isAccountEnabled;
    private final boolean isCredentialsNonExpired ;
    private final List<String> roles;

    public ApplicationUser(@NonNull final User user){
        this.grantedAuthorities = null ;
        this.username = user.getUserName();
        this.password = null;
        this.isAccountNonExpired = true;
        this.isAccountNonLocked = true;
        this.isAccountEnabled =true;
        this.isCredentialsNonExpired = true ;
        this.roles = user.getUserRoles();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.isAccountEnabled;
    }

    public List<String> getUserRoles() {
        return this.roles;
    }
}

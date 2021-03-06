package com.github.harboat.core.security;

import com.github.harboat.core.security.roles.Role;
import com.github.harboat.core.users.User;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

@SuppressFBWarnings(value = "SE_BAD_FIELD")
class UserDetailsImpl implements UserDetails {

    private final String username;

    private final String password;

    private final Role role;

    UserDetailsImpl(User user) {
        this.username = user.getId();
        this.password = user.getPassword();
        this.role = user.getRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        role.getAuthorities().forEach(a ->
                authorities.add(new SimpleGrantedAuthority(a.getName()))
        );
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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

}

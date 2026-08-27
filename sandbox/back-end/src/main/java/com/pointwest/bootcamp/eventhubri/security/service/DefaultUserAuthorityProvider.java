package com.pointwest.bootcamp.eventhubri.security.service;

import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import com.pointwest.bootcamp.eventhubri.identity.entity.User;

@Component
public class DefaultUserAuthorityProvider implements UserAuthorityProvider {
    private static final List<GrantedAuthority> DEFAULT_AUTHORITIES =
        List.of(new SimpleGrantedAuthority("ROLE_USER"));

    @Override
    public List<? extends GrantedAuthority> getAuthorities(User user) {
        return DEFAULT_AUTHORITIES;
    }
}

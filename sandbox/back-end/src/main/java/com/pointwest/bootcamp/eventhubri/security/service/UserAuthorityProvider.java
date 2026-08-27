package com.pointwest.bootcamp.eventhubri.security.service;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import com.pointwest.bootcamp.eventhubri.identity.entity.User;

public interface UserAuthorityProvider {
    Collection<? extends GrantedAuthority> getAuthorities(User user);
}

package com.pointwest.bootcamp.eventhubri.common.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.pointwest.bootcamp.eventhubri.entity.User;

@Component("auditorProvider")
public class SecurityAuditorAware implements AuditorAware<User> {
    
    @Override
    public Optional<User> getCurrentAuditor(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal() == null) {
            return Optional.empty();
        }

        if ("anonymousUser".equals(auth.getPrincipal())){
            return Optional.empty();
        }

        if (auth.getPrincipal() instanceof User currentUser){
            return Optional.of(currentUser);
        }

        return Optional.empty();
    }
}

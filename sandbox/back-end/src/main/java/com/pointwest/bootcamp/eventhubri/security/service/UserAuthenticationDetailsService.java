package com.pointwest.bootcamp.eventhubri.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pointwest.bootcamp.eventhubri.identity.entity.User;
import com.pointwest.bootcamp.eventhubri.identity.repository.UserRepository;

@Service
public class UserAuthenticationDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserAuthorityProvider authorityProvider;

    public UserAuthenticationDetailsService(
            UserRepository userRepository,
            UserAuthorityProvider authorityProvider) {
        this.userRepository = userRepository;
        this.authorityProvider = authorityProvider;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        String email = username == null ? "" : username.trim();
        User user = userRepository.findByEmailIgnoreCase(email)
            .orElseThrow(() -> new UsernameNotFoundException("Invalid email or password"));
        return new UserPrincipal(user, authorityProvider.getAuthorities(user));
    }
}

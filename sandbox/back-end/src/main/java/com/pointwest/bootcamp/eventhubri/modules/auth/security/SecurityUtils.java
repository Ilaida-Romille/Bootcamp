package com.pointwest.bootcamp.eventhubri.modules.auth.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.pointwest.bootcamp.eventhubri.modules.account.entity.AppUser;

@Component("securityUtils")
public class SecurityUtils {

    private SecurityUtils() {
    }

    public static SecurityUser getAuthenticatedUser(Authentication authentication) {
        // 1. Check if the principal is an instance of your own CustomUserDetails
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {

            // 2. Safely grab the underlying AppUser entity from your userDetails wrapper
            AppUser user = userDetails.getUser(); // Or however you expose the user in CustomUserDetails

            // 3. Map it to your clean SecurityUser record
            return new SecurityUser(
                    user.getId(),
                    user.getEmail(),
                    user.getOrganization().getId() // Make sure AppUser or CustomUserDetails has these getters
            );
        }

        throw new IllegalStateException("Invalid authentication principal: Expected CustomUserDetails");
    }

}

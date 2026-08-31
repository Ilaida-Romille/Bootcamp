package com.pointwest.bootcamp.eventhubri.modules.auth.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.lang.annotation.*;

@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
// This tells Spring to look at the CustomUserDetails principal, but we still
// need to map it
@AuthenticationPrincipal(expression = "@securityUtils.getAuthenticatedUser(authentication)")
public @interface CurrentUser {
}

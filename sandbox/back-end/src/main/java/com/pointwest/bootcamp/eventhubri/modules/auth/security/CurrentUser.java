package com.pointwest.bootcamp.eventhubri.modules.auth.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.lang.annotation.*;

@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
// This tells Spring to look at the CustomUserDetails principal, but we still
// need to map it
@AuthenticationPrincipal(expression = "new com.pointwest.bootcamp.eventhubri.modules.auth.security.SecurityUser(userId, username, organizationId)")
public @interface CurrentUser {
}

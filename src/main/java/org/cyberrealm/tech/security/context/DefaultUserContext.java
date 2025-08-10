package org.cyberrealm.tech.security.context;

import lombok.RequiredArgsConstructor;
import org.cyberrealm.tech.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultUserContext implements UserContext {
    @Override
    public User getUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}

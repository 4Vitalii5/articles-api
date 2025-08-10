package org.cyberrealm.tech.dto.user;

import lombok.Builder;

@Builder
public record UserResponseDto(
        Long id,
        String email,
        String firstName,
        String lastName
) {
}

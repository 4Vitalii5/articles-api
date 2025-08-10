package org.cyberrealm.tech.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cyberrealm.tech.service.impl.UserServiceImplTest.TestResources.ENCODED_PASSWORD;
import static org.cyberrealm.tech.service.impl.UserServiceImplTest.TestResources.NON_EXISTENT_USER_ID;
import static org.cyberrealm.tech.service.impl.UserServiceImplTest.TestResources.USER_EMAIL;
import static org.cyberrealm.tech.service.impl.UserServiceImplTest.TestResources.USER_ID;
import static org.cyberrealm.tech.service.impl.UserServiceImplTest.TestResources.buildNewUser;
import static org.cyberrealm.tech.service.impl.UserServiceImplTest.TestResources.buildRegistrationRequest;
import static org.cyberrealm.tech.service.impl.UserServiceImplTest.TestResources.buildUserRole;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.cyberrealm.tech.dto.user.UserRegistrationRequestDto;
import org.cyberrealm.tech.dto.user.UserResponseDto;
import org.cyberrealm.tech.exception.EntityNotFoundException;
import org.cyberrealm.tech.exception.RegistrationException;
import org.cyberrealm.tech.mapper.UserMapperImpl;
import org.cyberrealm.tech.model.Role;
import org.cyberrealm.tech.model.User;
import org.cyberrealm.tech.repository.RoleRepository;
import org.cyberrealm.tech.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private UserMapperImpl userMapper;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void givenValidRequest_register_shouldSucceed() throws RegistrationException {
        // Given
        UserRegistrationRequestDto requestDto = buildRegistrationRequest();

        when(userRepository.findByEmail(requestDto.email())).thenReturn(Optional.empty());
        when(roleRepository.findByRole(Role.RoleName.ROLE_USER))
                .thenReturn(Optional.of(buildUserRole()));
        when(passwordEncoder.encode(requestDto.password())).thenReturn(ENCODED_PASSWORD);
        when(userRepository.save(any(User.class))).thenReturn(buildNewUser());

        // When
        UserResponseDto actualResponse = userService.register(requestDto);

        // Then
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.email()).isEqualTo(requestDto.email());

        verify(userRepository).findByEmail(requestDto.email());
        verify(passwordEncoder).encode(requestDto.password());
    }

    @Test
    void register_withExistingEmail_shouldThrowException() {
        // Given
        UserRegistrationRequestDto requestDto = buildRegistrationRequest();

        when(userRepository.findByEmail(requestDto.email()))
                .thenReturn(Optional.of(buildNewUser()));

        // When
        assertThrows(RegistrationException.class, () -> userService.register(requestDto));

        // Then
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_whenDefaultRoleNotFound_shouldThrowException() {
        // Given
        UserRegistrationRequestDto requestDto = buildRegistrationRequest();
        when(userRepository.findByEmail(requestDto.email())).thenReturn(Optional.empty());
        when(roleRepository.findByRole(Role.RoleName.ROLE_USER)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            userService.register(requestDto);
        });
    }

    @Test
    void findById_withExistingUser_shouldReturnUserDto() {
        // Given
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(buildNewUser()));

        // When
        UserResponseDto actualDto = userService.findById(USER_ID);

        // Then
        assertThat(actualDto).isNotNull();
        assertThat(actualDto.id()).isEqualTo(USER_ID);
        assertThat(actualDto.email()).isEqualTo(USER_EMAIL);
    }

    @Test
    void findById_withNonExistingUser_shouldThrowException() {
        // Given
        when(userRepository.findById(NON_EXISTENT_USER_ID)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            userService.findById(NON_EXISTENT_USER_ID);
        });
    }

    static class TestResources {
        static final String ENCODED_PASSWORD = "encodedPassword";
        static final Long USER_ID = 1L;
        static final Long NON_EXISTENT_USER_ID = 99L;
        static final Long ROLE_ID = 1L;
        static final String USER_EMAIL = "user@ukr.net";
        static final String USER_PASSWORD = "password";
        static final String FIRST_NAME = "Vitalii";
        static final String LAST_NAME = "Stepanuk";

        static UserRegistrationRequestDto buildRegistrationRequest() {
            return new UserRegistrationRequestDto(
                    USER_EMAIL,
                    USER_PASSWORD,
                    USER_PASSWORD,
                    FIRST_NAME,
                    LAST_NAME
            );
        }

        static User buildNewUser() {
            return User.builder()
                    .id(USER_ID)
                    .email(USER_EMAIL)
                    .password(USER_PASSWORD)
                    .firstName(FIRST_NAME)
                    .lastName(LAST_NAME)
                    .build();
        }

        static Role buildUserRole() {
            Role role = new Role();
            role.setId(ROLE_ID);
            role.setRole(Role.RoleName.ROLE_USER);

            return role;
        }
    }
}

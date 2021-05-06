package com.theater.movies.service;

import com.theater.movies.entity.OffsetBasedPageRequest;
import com.theater.movies.entity.UserEntity;
import com.theater.movies.enums.LoginStatus;
import com.theater.movies.enums.Status;
import com.theater.movies.exception.BadArgumentException;
import com.theater.movies.exception.DuplicateUsernameException;
import com.theater.movies.model.CommonResponse;
import com.theater.movies.model.PageableRequest;
import com.theater.movies.model.Response;
import com.theater.movies.model.User;
import com.theater.movies.repository.UserRepository;
import com.theater.movies.util.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.theater.movies.util.PageRequestUtil.checkPageableRequestIfValid;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Resource(name = "tokenServices")
    private final ConsumerTokenServices tokenServices;

    public Response createUser(User user, HttpServletRequest request) {
        checkDuplicateUser(user.getUsername());

        return ResponseBuilder.buildResponse(toModel(userRepository.save(UserEntity.builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .createdAt(LocalDateTime.now())
                .createdBy(request.getUserPrincipal().getName())
                .loginStatus(LoginStatus.OFFLINE)
                .memo(user.getMemo())
                .status(Status.ACTIVE)
                .build())));
    }

    public Response register(User user) {
        checkDuplicateUser(user.getUsername());

        return ResponseBuilder.buildResponse(toModel(userRepository.save(UserEntity.builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .createdAt(LocalDateTime.now())
                .loginStatus(LoginStatus.OFFLINE)
                .memo(user.getMemo())
                .status(Status.ACTIVE)
                .build())));
    }

    public CommonResponse deleteUser(Long id) {
        var userEntity = checkUserExistence(id);

        userRepository.deleteById(userEntity.getId());
        return CommonResponse.builder()
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .message("Successfully deleted user.")
                .build();
    }

    public CommonResponse updateUser(Long id, User user, HttpServletRequest request) {
        var userEntity = checkUserExistence(id);

        checkDuplicateUser(user.getUsername());

        Optional.of(user.getUsername())
                .ifPresent(userEntity::setUsername);
        Optional.ofNullable(user.getPassword())
                .ifPresent(password -> userEntity.setPassword(passwordEncoder.encode(password)));
        Optional.ofNullable(user.getMemo())
                .ifPresent(userEntity::setMemo);

        userEntity.setUpdatedAt(LocalDateTime.now());
        userEntity.setUpdatedBy(request.getUserPrincipal().getName());

        return ResponseBuilder.buildResponse(toModel(userRepository.save(userEntity)));
    }

    public CommonResponse updateUserStatus(Long id, Status status, HttpServletRequest request) {
        var userEntity = checkUserExistence(id);

        Optional.of(status)
                .ifPresent(userEntity::setStatus);

        userEntity.setUpdatedAt(LocalDateTime.now());
        userEntity.setUpdatedBy(request.getUserPrincipal().getName());

        return ResponseBuilder.buildResponse(toModel(userRepository.save(userEntity)));
    }

    public Response getUsers(PageableRequest pageableRequest) {
        checkPageableRequestIfValid(pageableRequest);

        return ResponseBuilder.buildResponse(StreamSupport.stream(userRepository.findAll(
                new OffsetBasedPageRequest(pageableRequest.getOffset(), pageableRequest.getLimit())).spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.toList()));
    }

    public Response getUser(Long id) {
        return ResponseBuilder.buildResponse(toModel(userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with id " + id + " not found."))));
    }

    public CommonResponse logout(HttpServletRequest request) {
        var authorization = request.getHeader("Authorization");
        var user = userRepository.findByUsername(request.getUserPrincipal().getName());

        if (authorization != null && authorization.contains("Bearer")){
            String tokenId = authorization.substring("Bearer".length()+1);
            tokenServices.revokeToken(tokenId);

            user.ifPresent(userEntity -> {
                userEntity.setLoginStatus(LoginStatus.OFFLINE);
                userRepository.save(userEntity);
            });

            return CommonResponse.builder()
                    .status(HttpStatus.OK)
                    .message("Successfully logout.")
                    .timestamp(LocalDateTime.now())
                    .build();
        }

        throw new BadArgumentException("Cannot recognize bearer token provided.");
    }

    public Response changePassword(String oldPassword, String newPassword, HttpServletRequest request) {
        var username = request.getUserPrincipal().getName();
        var userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadArgumentException("User with username " + username + "does not exists."));

        if (passwordEncoder.matches(oldPassword, userEntity.getPassword())) {
            userEntity.setPassword(passwordEncoder.encode(newPassword));
            userEntity.setUpdatedAt(LocalDateTime.now());
            userEntity.setUpdatedBy(request.getUserPrincipal().getName());
            return ResponseBuilder.buildResponse(toModel(userRepository.save(userEntity)));
        }
        throw new BadArgumentException("Old password does not match.");
    }

    private UserEntity checkUserExistence(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BadArgumentException("User with id " + id + "does not exists."));
    }

    private void checkDuplicateUser(String username) {
        userRepository.findByUsername(username)
                .ifPresent(foundUser -> {
                    throw new DuplicateUsernameException("Duplicate username with " + foundUser.getUsername() + " found! Username must be unique.");
                });
    }

    private User toModel(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .createdAt(userEntity.getCreatedAt())
                .createdBy(userEntity.getCreatedBy())
                .loginStatus(userEntity.getLoginStatus())
                .memo(userEntity.getMemo())
                .status(userEntity.getStatus())
                .updatedAt(userEntity.getUpdatedAt())
                .updatedBy(userEntity.getUpdatedBy())
                .build();
    }

}

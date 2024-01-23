package br.com.askcode.askcode.Service;


import br.com.askcode.askcode.Config.Constants.LoginServiceConstants;
import br.com.askcode.askcode.Config.JwtConfig.Jwt;
import br.com.askcode.askcode.Domain.Request.UserLoginRequest;
import br.com.askcode.askcode.Domain.Response.JwtResponse;
import br.com.askcode.askcode.Entity.UserEntity;
import br.com.askcode.askcode.Exception.Runtime.CreateErrorResponse;
import br.com.askcode.askcode.Exception.Runtime.InvalidPasswordException;
import br.com.askcode.askcode.Exception.Runtime.UserNotFoundException;
import br.com.askcode.askcode.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@ControllerAdvice
@Slf4j
public class LoginService {
    private final UserRepository userRepository;
    private final Jwt jwt;
    private final BCryptPasswordEncoder passwordEncoder;

    public ResponseEntity<Object> loginUser(@Valid UserLoginRequest user) {
        try {
            log.info("loginUser() - Iniciando login do usuário - email: {}", user.getEmail());

            UserEntity userEntity = getUserEmail(user.getEmail());
            validateUserCredentials(user, userEntity);

            UserDetails userDetails = new User(userEntity.getEmail(), userEntity.getPassword(), Collections.emptyList());

            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));

            String token = this.jwt.generateJwtToken(userDetails, user);

            log.info("Login do usuário realizado com sucesso - email: {}", user.getEmail());
            return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse(token));
        } catch (UserNotFoundException | InvalidPasswordException e) {
            log.error("loginUser() - Erro durante o login do usuário - mensagem: {}", e.getMessage());
            return CreateErrorResponse.response(HttpStatus.BAD_REQUEST, e.getMessage(), LoginServiceConstants.URI_LOGIN_USER);
        } catch (Exception e) {
            log.error("loginUser() - Erro inesperado durante o login do usuário", e);
            return CreateErrorResponse.response(HttpStatus.INTERNAL_SERVER_ERROR, LoginServiceConstants.ERRO_APPLICATION, LoginServiceConstants.URI_LOGIN_USER);
        }
    }

    private void validateUserCredentials(UserLoginRequest user, UserEntity userEntity) {
        if (userEntity == null) {
            log.error("validateUserCredentials() - O email informado está incorreto ou a conta não existe.");
            throw new UserNotFoundException(LoginServiceConstants.USER_NOT_FOUND);
        }

        if (!checkPassword(user.getPassword(), userEntity.getPassword())) {
            log.error("validateUserCredentials() - A senha informada está incorreta.");
            throw new InvalidPasswordException(LoginServiceConstants.PASSWORD_ERROR);
        }
    }


    private boolean checkPassword(String request, String encodedPassword) {
        return this.passwordEncoder.matches(request, encodedPassword);
    }

    private UserEntity getUserEmail(String email) {
        Optional<UserEntity> userOptional = this.userRepository.findByEmail(email);
        return userOptional.orElse(null);
    }
}


package br.com.askcode.askcode.Service;


import br.com.askcode.askcode.Config.Constants.UserServiceConstants;
import br.com.askcode.askcode.Entity.UserEntity;
import br.com.askcode.askcode.Exception.Runtime.DuplicateEmailException;
import br.com.askcode.askcode.Exception.Runtime.DuplicateUsernameException;
import br.com.askcode.askcode.Exception.Runtime.UserServiceException;
import br.com.askcode.askcode.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@ControllerAdvice
public class UserService {

    private final UserRepository userRepository;

    public UserEntity saveUser(UserEntity user) {
        try {
            log.info("saveUser() - Starting saving new user - username : {}", user.getEmail());
            Optional<UserEntity> optionalUsername = this.userRepository.findByUsername(user.getUsername());
            checkUsernameAlreadyExists(optionalUsername, user);

            Optional<UserEntity> optionalEmail = this.userRepository.findByEmail(user.getEmail());
            checkEmailAlreadyExists(optionalEmail, user);

            return this.userRepository.save(user);
        } catch (DuplicateUsernameException | DuplicateEmailException ex) {
            log.error("saveUser() - Error when saving user", ex);
            throw ex;
        } catch (UserServiceException exception) {
            log.error("saveUser() - Unexpected error when saving user - message:{}", exception.getMessage());
            throw new UserServiceException(exception.getMessage());
        }
    }

    private void checkUsernameAlreadyExists(Optional<UserEntity> optionalUser, UserEntity userEntity) {
        if (optionalUser.isPresent()) {
            log.info("checkUsernameAlreadyExists() - Username already registed - username : {}", userEntity.getUsername());
            throw new DuplicateUsernameException(String.format(UserServiceConstants.USERNAME_EXISTS, userEntity.getUsername()));
        }
    }

    private void checkEmailAlreadyExists(Optional<UserEntity> optionalUser, UserEntity userEntity) {
        if (optionalUser.isPresent()) {
            log.info("checkEmailAlreadyExists() - Email already registed - email : {}", userEntity.getEmail());
            throw new DuplicateEmailException(String.format(UserServiceConstants.USERNAME_EXISTS, userEntity.getEmail()));
        }
    }
}

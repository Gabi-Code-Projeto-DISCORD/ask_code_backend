package br.com.askcode.askcode.Domain.Mapper;


import br.com.askcode.askcode.Domain.Request.UserCreateRequest;
import br.com.askcode.askcode.Domain.Response.UserResponse;
import br.com.askcode.askcode.Entity.UserEntity;
import br.com.askcode.askcode.Exception.Runtime.UserServiceException;
import br.com.askcode.askcode.Service.UserService;
import br.com.askcode.askcode.Util.ValidatorEmailUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@AllArgsConstructor
public class UserMapper {
    @Autowired
    private final ModelMapper modelMapper;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final UserService userService;

    public UserResponse toUserResponse(UserEntity user) {
        return this.modelMapper.map(user, UserResponse.class);
    }

    public UserEntity toUserRequest(UserCreateRequest createRequest) {
        if (createRequest.getUsername().isEmpty() || createRequest.getPassword().isEmpty()) {
            log.info("toUserRequest() - Empty username or passwoord in user :{}", createRequest);
            throw new UserServiceException("Empty username or passwoord in user " + createRequest.getUsername());
        }
        createRequest.setId(UUID.randomUUID());
        createRequest.getEmail();
        createRequest.setPassword(passwoord(createRequest.getPassword()));
        ValidatorEmailUtil validatorEmailUtil = new ValidatorEmailUtil();
        if (!validatorEmailUtil.validate(createRequest.getEmail())) {
            log.info("toUserRequest() - Inválido email : {}", createRequest.getEmail());
            throw new UserServiceException("Inválido email: " + createRequest.getEmail());
        }
        return this.modelMapper.map(createRequest, UserEntity.class);
    }


    private String passwoord(String password) {
        return this.passwordEncoder.encode(password);
    }
}

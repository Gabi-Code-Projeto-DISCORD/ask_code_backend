package br.com.askcode.askcode.Controller;

import br.com.askcode.askcode.Domain.Mapper.UserMapper;
import br.com.askcode.askcode.Domain.Request.UserCreateRequest;
import br.com.askcode.askcode.Domain.Response.ErrorResponse;
import br.com.askcode.askcode.Domain.Response.UserResponse;
import br.com.askcode.askcode.Exception.Runtime.CreateErrorResponse;
import br.com.askcode.askcode.Exception.Runtime.UserServiceException;
import br.com.askcode.askcode.Service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
@Api(value = "API REST USER")
@ControllerAdvice
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Usuario criado com sucesso !"),
            @ApiResponse(code = 403, message = "Você não tem permissão !", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "Nome já cadastrado !", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "O servidor encontrou uma condição inesperada que o impediu de atender à solicitação.", response = ErrorResponse.class),
    })
    @PostMapping(value = "/save-user")
    @ApiOperation(
            value = "API REST - Create USER",
            response = UserResponse.class
    )
    public ResponseEntity<Object> saveUser(@Valid @RequestBody UserCreateRequest createRequest) {
        try {
            log.info("saveUser() - Iniciando saveUser");

            Optional<UserResponse> optional = Stream.of(createRequest)
                    .map(this.userMapper::toUserRequest)
                    .map(this.userService::saveUser)
                    .map(this.userMapper::toUserResponse)
                    .findFirst();

            if (optional.isEmpty()) {
                log.info("saveUser() - Nenhum dado encontrado para salvar");
                return CreateErrorResponse.response(HttpStatus.NOT_FOUND, "Nenhum dado encontrado para salvar !", "/api/save-user");
            }

            log.info("saveUser() - Concluído saveUser");
            return ResponseEntity.status(HttpStatus.CREATED).body(optional.get());
        } catch (UserServiceException e) {
            log.info("saveUser() - Erro interno ao salvar usuário - mensagem:{}", e.getMessage());
            return CreateErrorResponse.response(HttpStatus.BAD_REQUEST, e.getMessage(), "/api/save-user");
        }
    }
}

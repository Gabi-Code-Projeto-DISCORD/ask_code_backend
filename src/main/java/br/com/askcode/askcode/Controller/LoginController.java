package br.com.askcode.askcode.Controller;

import br.com.askcode.askcode.Domain.Request.UserLoginRequest;
import br.com.askcode.askcode.Service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
@Api(value = "API REST Login user")
@ControllerAdvice
public class LoginController {
    private final LoginService loginService;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Usuario logado com sucesso !"),
            @ApiResponse(code = 403, message = "Você não tem permissão !"),
            @ApiResponse(code = 404, message = "Erro ao realizar login !"),
            @ApiResponse(code = 500, message = "O servidor encontrou uma condição inesperada que o impediu de atender à solicitação."),
    })
    @PostMapping(value = "/login")
    @ApiOperation(value = "API REST - Login USER")
    public ResponseEntity<Object> loginUser(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        return this.loginService.loginUser(userLoginRequest);
    }
}

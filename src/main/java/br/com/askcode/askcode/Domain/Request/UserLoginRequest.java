package br.com.askcode.askcode.Domain.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequest {
    @NotBlank(message = "Campo de email não pode ser vazio")
    private String email;
    @NotBlank(message = "Campo de password não pode ser vazio.")
    private String password;
}

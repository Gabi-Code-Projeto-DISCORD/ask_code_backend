package br.com.askcode.askcode.Domain.Request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {
    @JsonIgnore
    private UUID id;

    @NotBlank(message = "Campo username não poode ser vazio.")
    @Size(min = 1, max = 55, message = "O campo username deve ter entre 1 e 55 caracteres.")
    private String username;

    @NotBlank(message = "Campo password não pode ser vazio.")
    @Size(min = 1, max = 30, message = "O campo email deve ter entre 1 e 30 caracteres.")
    private String password;

    @NotBlank(message = "Campo email não deve ser vazio.")
    @Size(min = 1, max = 55, message = "O campo email deve ter entre 1 e 55 caracteres")
    private String email;
}

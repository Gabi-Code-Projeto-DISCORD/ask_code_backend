package br.com.askcode.askcode.Entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "ask_database", name = "usuario")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = -8732691332126075333L;

    @ApiModelProperty(value = "Codigo de registro")
    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true, columnDefinition = "VARCHAR(60)")
    protected UUID id;

    @ApiModelProperty(value = "Nome da pessoa")
    @Column(name = "username", nullable = false, columnDefinition = "VARCHAR(120)")
    private String username;

    @ApiModelProperty(value = "E-mail da pessoa")
    @Column(name = "email", nullable = false, columnDefinition = "VARCHAR(60)")
    private String email;

    @ApiModelProperty(value = "Senha da pessoa")
    @Column(name = "password", nullable = false, columnDefinition = "VARCHAR(60)")
    private String password;
}

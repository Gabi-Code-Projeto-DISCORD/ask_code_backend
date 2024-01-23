package br.com.askcode.askcode.Config.JwtConfig;

import br.com.askcode.askcode.Config.Constants.AuthConstants;
import br.com.askcode.askcode.Domain.Request.UserLoginRequest;
import br.com.askcode.askcode.Entity.UserEntity;
import br.com.askcode.askcode.Exception.Runtime.UserServiceException;
import br.com.askcode.askcode.Repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;


@Slf4j
@Configuration
@AllArgsConstructor
public class Jwt {
    private final UserRepository userRepository;

    public String generateJwtToken(UserDetails userDetails, UserLoginRequest user) {
        log.info("generateJwtToken() - Iniciando a geração do token JWT - email: {}", user.getEmail());
        Optional<UserEntity> optionalUser = this.userRepository.findByEmail(user.getEmail());

        if (!optionalUser.isPresent()) {
            log.info("generateJwtToken() - Falha ao gerar o token JWT. Email não encontrado com username: {}", user.getEmail());
            throw new UserServiceException("Falha ao gerar o token JWT. Email não encontrado com username: " + user.getEmail());
        }

        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        claims.put("auth", userDetails.getAuthorities().stream()
                .map(s -> new SimpleGrantedAuthority(null))
                .filter(Objects::nonNull)
                .toList());

        Date now = new Date();
        Date validity = new Date(now.getTime() + AuthConstants.EXPIRATION_TIME);

        JwtBuilder jwt = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setSubject(user.getEmail())
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, AuthConstants.SECRET_KEY);

        log.info("generateJwtToken() - Concluindo a geração do token json web - autenticação: {}", userDetails.getUsername());

        return jwt.compact();
    }
}

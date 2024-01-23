package br.com.askcode.askcode.Config.Auth;

import br.com.askcode.askcode.Config.Constants.AuthConstants;
import br.com.askcode.askcode.Config.Data.DetalherUserData;
import br.com.askcode.askcode.Util.PasswordGeneratorUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Slf4j
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain, Authentication authentication) throws IOException {
        logger.info("successfulAuthentication() - Starting authentication ");
        DetalherUserData data = (DetalherUserData) authentication.getPrincipal();
        String newSecret = PasswordGeneratorUtil.generateNewSecretKey();
        Algorithm algorithm = Algorithm.HMAC512(newSecret);
        String token = JWT.create()
                .withSubject(data.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + AuthConstants.EXPIRATION_TIME))
                .sign(algorithm);
        String body = ((DetalherUserData) authentication.getPrincipal()).getUsername() + " " + token;
        logger.info("successfulAuthentication() - Finished Authentication ");
        httpServletResponse.getWriter().write(body);
        httpServletResponse.getWriter().flush();
    }
}

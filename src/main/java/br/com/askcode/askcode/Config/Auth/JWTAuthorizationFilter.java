package br.com.askcode.askcode.Config.Auth;

import br.com.askcode.askcode.Config.Constants.AuthConstants;
import br.com.askcode.askcode.Config.JwtConfig.Jwt;
import br.com.askcode.askcode.Exception.Runtime.UserServiceException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Slf4j
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private Jwt jwt;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("doFilterInternal() - Starting validation authorization");
        String header = response.getHeader(AuthConstants.HEADER_STRING);
        try {
            if (header == null || !header.startsWith(AuthConstants.TOKEN_PREFIX)) {
                chain.doFilter(request, response);
                return;
            }
            String token = header.replace(AuthConstants.TOKEN_PREFIX, "");
            Claims claims = Jwts.parser().setSigningKey(AuthConstants.SECRET_KEY).parseClaimsJws(token).getBody();
            String username = claims.getSubject();
            if (username == null) {
                log.info("doFilterInternal() - email is null");
                throw new UserServiceException("doFilterInternal() - email is null");
            }
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.info("doFilterInternal() - Finished validation user - message:{}", request.getRequestURI());

            chain.doFilter(request, response);
        } catch (Exception e) {
            log.info("doFilterInternal() - Error validation user - message:{}", e.getMessage());
            SecurityContextHolder.clearContext();
            response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}

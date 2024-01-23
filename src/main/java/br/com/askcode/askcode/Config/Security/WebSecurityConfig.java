package br.com.askcode.askcode.Config.Security;

import br.com.askcode.askcode.Config.Auth.JWTAuthenticationFilter;
import br.com.askcode.askcode.Config.Auth.JWTAuthorizationFilter;
import br.com.askcode.askcode.Config.Constants.WebSecurityConfigConstants;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
@NoArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors().and()
                .csrf().ignoringAntMatchers(WebSecurityConfigConstants.URI_API)
                .requireCsrfProtectionMatcher(new AntPathRequestMatcher(WebSecurityConfigConstants.URI_API))
                .and()
                .authorizeRequests()
                // SWAGGER
                .antMatchers(WebSecurityConfigConstants.URI_SAVE_USER).permitAll()
                .antMatchers(WebSecurityConfigConstants.URI_SWAGGER_UI).permitAll()
                .antMatchers(WebSecurityConfigConstants.URI_SWAGGER_API_DOCS).permitAll()
                .antMatchers(WebSecurityConfigConstants.URI_SWAGGER_RESOURCES).permitAll()
                .antMatchers(WebSecurityConfigConstants.URI_SWAGGER_CONFIGURATION).permitAll()
                .antMatchers(WebSecurityConfigConstants.URI_SWAGGER_WEBJARS).permitAll()
                // ROTAS
                .antMatchers(HttpMethod.POST, WebSecurityConfigConstants.URI_LOGIN_USER).permitAll()

                .anyRequest().authenticated()
                .and()
                .headers().cacheControl();

        // AUTH
        httpSecurity.addFilter(new JWTAuthorizationFilter(authenticationManager()));
        httpSecurity.addFilter(new JWTAuthenticationFilter(authenticationManager()));
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList(WebSecurityConfigConstants.URL_LOCAL));
        corsConfiguration.setAllowedMethods(Arrays.asList(
                WebSecurityConfigConstants.METHOD_GET,
                WebSecurityConfigConstants.METHOD_POST
        ));
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }
}

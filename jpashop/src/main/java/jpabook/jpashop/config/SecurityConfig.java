package jpabook.jpashop.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import jpabook.jpashop.domain.UserRole;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.MyUserDetailsService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig{

    private final MyUserDetailsService myUserDetailsService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 권한(hasRole)을 사용할때는 자동으로 앞에 ROLE_를 붙여서 검사한다..
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http
                .csrf((csrfConfig) ->
                        csrfConfig.disable()
                )
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers("/order/**").hasRole("USER")
                                .anyRequest().permitAll()
                )
                .formLogin((formLogin) ->
                        formLogin
                                .loginPage(("/member/login"))
                                .usernameParameter("email")
                                .passwordParameter("password")
                                .loginProcessingUrl("/member/login")
                                .defaultSuccessUrl("/",true)
                )
                .logout((logoutConfig) ->
                        logoutConfig.logoutSuccessUrl("/")
                )
                .userDetailsService(myUserDetailsService)
                .exceptionHandling((exceptionConfig) ->
                        exceptionConfig
                                .authenticationEntryPoint(unauthorizedEntryPoint)
                                .accessDeniedHandler(accessDeniedHandler)
                ); // 401 403 관련 예외처리;
        return http.build();
    }

    private final AuthenticationEntryPoint unauthorizedEntryPoint =
            (request, response, authException) -> {
                ErrorResponse fail = new ErrorResponse(HttpStatus.UNAUTHORIZED, "Spring security unauthorized...");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                String json = new ObjectMapper().writeValueAsString(fail);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                PrintWriter writer = response.getWriter();
                writer.write(json);
                writer.flush();
            };

    private final AccessDeniedHandler accessDeniedHandler = (request, response, accessDeniedException) -> {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "anonymous";
        List<String> roles = new ArrayList<>();
        if (authentication != null) {
            username = authentication.getName();
            roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
        }
        String message = String.format("User %s with roles %s does not have access to this resource.", username, roles);
        ErrorResponse fail = new ErrorResponse(HttpStatus.FORBIDDEN, message);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String json = new ObjectMapper().writeValueAsString(fail);
        PrintWriter writer = response.getWriter();
        writer.write(json);
        writer.flush();
    };


    @Getter
    @RequiredArgsConstructor
    public class ErrorResponse {

        private final HttpStatus status;
        private final String message;
    }

}

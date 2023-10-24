package cyy.greenblue.security;

import cyy.greenblue.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

    private final PrincipalOauth2UserService userService; // 사용자 정보를 가져오고 처리하기 위한 설정

    @Bean
    public BCryptPasswordEncoder encoderPw() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http
                .authorizeHttpRequests((requests) -> requests
                        .antMatchers("/", "/join").permitAll()
                        .antMatchers("/user/**").hasAnyRole("USER", "ADMIN", "MANAGER")
                        .antMatchers("/admin/**").hasAnyRole("ADMIN", "MANAGER")
                        .antMatchers("/manager/**").hasRole("MANAGER")
                        .anyRequest().authenticated())
                .formLogin((form) -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/loginProc")
                        .defaultSuccessUrl("/hello")
                        .permitAll())
                .logout((logout) -> logout.permitAll())
                .oauth2Login((google) -> google
                        .loginPage("/loginForm")
                        .userInfoEndpoint() //인증 후 사용자 정보 가져오는 엔드포인트 설정
                        .userService(userService)); //사용자 정보를 어떻게 처리할 건지 결정하는 사용자 정의 서비스 설정

        return http.build();
    }

}

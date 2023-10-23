package cyy.greenblue.auth;

import cyy.greenblue.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터체인에 등록됨
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final PrincipalOauth2UserService userService; // 사용자 정보를 가져오고 처리하기 위한 설정

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http
                .authorizeHttpRequests((requests) -> requests
                        .antMatchers("/", "/home", "/join").permitAll()
                        .anyRequest().authenticated())
//                .authorizeRequests((requests) -> requests
//                        .antMatchers("/user/**").authenticated()
//                        .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
//                        .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
//                        .anyRequest().permitAll())
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll())
                .logout((logout) -> logout.permitAll())
                .oauth2Login((google) -> google
                        .loginPage("/loginForm")
                        .userInfoEndpoint() //인증 후 사용자 정보 가져오는 엔드포인트 설정
                        .userService(userService)); //사용자 정보를 어떻게 처리할 건지 결정하는 사용자 정의 서비스 설정

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("password")
                        .roles("USER")
                        .build();
        return new InMemoryUserDetailsManager(user);
    }
}

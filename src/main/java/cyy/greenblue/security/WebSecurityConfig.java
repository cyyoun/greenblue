package cyy.greenblue.security;

import cyy.greenblue.security.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
/**
 * 1. 코드 받기(인증 완료)
 * 2. 액세스 토큰 (권한)
 * 3. 사용자 프로필 정보를 가져오기
 * 4. 가져온 정보를 토대로 회원가입 자동 진행시키기
 */

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

    private final PrincipalOauth2UserService principalOauth2UserService; // 사용자 정보를 가져오고 처리하기 위한 설정

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
                // 구글 로그인 완료 후 후처리가 필요함 → Tip. 액세스 토큰 + 사용자 프로필 정보 받음
                .oauth2Login((google) -> google
                        .loginPage("/loginForm")
                        .userInfoEndpoint() //인증 후 사용자 정보 가져오는 엔드포인트 설정
                        .userService(principalOauth2UserService) //사용자 정보를 어떻게 처리할 건지 결정하는 사용자 정의 서비스 설정
                        .and().defaultSuccessUrl("/hello", true));

        return http.build();
    }

}

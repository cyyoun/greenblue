package cyy.greenblue.security;

import cyy.greenblue.repository.MemberRepository;
import cyy.greenblue.security.auth.PrincipalAuthenticationManager;
import cyy.greenblue.security.auth.PrincipalAuthenticationProvider;
import cyy.greenblue.security.auth.PrincipalDetailsService;
import cyy.greenblue.security.jwt.JwtAuthenticationFilter;
import cyy.greenblue.security.jwt.JwtAuthorizationFilter;
import cyy.greenblue.security.jwt.JwtProperties;
import cyy.greenblue.security.jwt.JwtUtil;
import cyy.greenblue.security.oauth.PrincipalOauth2UserService;
import cyy.greenblue.service.JwtTokenService;
import cyy.greenblue.service.MemberService;
import io.jsonwebtoken.io.Encoders;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

/**
 * 1. 코드 받기(인증 완료)
 * 2. 액세스 토큰 (권한)
 * 3. 사용자 프로필 정보를 가져오기
 * 4. 가져온 정보를 토대로 회원가입 자동 진행시키기
 */

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

    private final PrincipalOauth2UserService principalOauth2UserService; // 사용자 정보를 가져오고 처리하기 위한 설정
    private final CorsFilter corsFilter;
    private final PrincipalDetailsService principalDetailsService;
    private final JwtTokenService jwtTokenService;
    private final MemberService memberService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrfConfigurer -> csrfConfigurer.disable())
                .addFilterBefore(corsFilter, CorsFilter.class);
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/favicon.ico").permitAll()
                        .requestMatchers("/login", "/join", "/auth/loginProc", "/error/**").permitAll()
                        .requestMatchers("/review/**", "/point/**", "/cart/**", "/product/**",
                                "/category/**", "/member/**", "/order/**").hasAnyRole("USER", "ADMIN", "MANAGER")
                        .requestMatchers("/auth/hello").hasAnyRole("USER", "ADMIN", "MANAGER")
                        .requestMatchers("/manager/**").hasRole("MANAGER")
                        .anyRequest().authenticated())
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(LogoutConfigurer::permitAll);

                // 구글 로그인 완료 후 후처리가 필요함 → Tip. 액세스 토큰 + 사용자 프로필 정보 받음
//                .oauth2Login((oauth) -> oauth
//                        .loginPage("/login")
//                        .defaultSuccessUrl("/auth/hello").permitAll()
//                        .userInfoEndpoint(userInfoEndpointConfig ->
//                                userInfoEndpointConfig.userService(principalOauth2UserService)));

        http.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilter(authorizationFilter());
        return http.build();

    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtUtil jwtUtil() {
        byte[] bytes = JwtProperties.SECRET.getBytes();
        String secret = Encoders.BASE64.encode(bytes);
        return new JwtUtil(secret, memberService, jwtTokenService);
    }

    @Bean
    public PrincipalAuthenticationProvider authenticationProvider() {
        return new PrincipalAuthenticationProvider(principalDetailsService, passwordEncoder());
    }

    @Bean
    public PrincipalAuthenticationManager authenticationManager() {
        return new PrincipalAuthenticationManager(authenticationProvider());
    }

    @Bean
    public JwtAuthenticationFilter authenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil(), authenticationManager());
    }

    @Bean JwtAuthorizationFilter authorizationFilter() {
        return new JwtAuthorizationFilter(authenticationManager(), jwtUtil(), memberService);
    }
}

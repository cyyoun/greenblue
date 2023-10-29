package cyy.greenblue.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    // @CrossOrigin : 로그인 인증이 필요 없을 때 사용
    // CorsFilter : 로그인 인증이 필요할 때 사용

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); //사용자의 정보가 도메인에 포함되어 요청, 응답 가능
        config.addAllowedOrigin("*"); //모든 ip 허용
        config.addAllowedHeader("*"); //모든 header에 응답 허용
        config.addAllowedMethod("*"); //모든 HTTP 메서드 요청 허용
        source.registerCorsConfiguration("/login/**", config); // /api/** 로 들어오는 값 모두 설정
        return new CorsFilter(source);

    }
}

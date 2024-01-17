package cyy.greenblue.security.jwt;

import cyy.greenblue.security.auth.PrincipalDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final PrincipalDetailsService principalDetailsService;
    private final JwtUtil jwtUtil;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, PrincipalDetailsService principalDetailsService, JwtUtil jwtUtil) {
        super(authenticationManager);
        this.principalDetailsService = principalDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("===============================JwtAuthorizationFilter.doFilterInternal===============================");
        System.out.println("request.getContextPath() = " + request.getRequestURI());
        Cookie[] cookies = request.getCookies();
        String token = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt")) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        if (token != null) {
            if (!jwtUtil.validateToken(token)) {
                throw new RuntimeException("[권한 없음] 유효하지 않은 토큰입니다...");
            }
            String username = jwtUtil.findUsernameByToken(token);

            if (username != null) {
                UserDetails principalDetails = principalDetailsService.loadUserByUsername(username);
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}

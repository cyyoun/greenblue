package cyy.greenblue.security.jwt;

import cyy.greenblue.domain.Member;
import cyy.greenblue.security.auth.PrincipalDetails;
import cyy.greenblue.service.MemberService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtUtil jwtUtil;
    private final MemberService memberService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, MemberService memberService) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.memberService = memberService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("===============================JwtAuthorizationFilter.doFilterInternal===============================");

        String requestHeader = request.getHeader(JwtProperties.HEADER);

        if (requestHeader != null && requestHeader.startsWith(JwtProperties.TOKEN_PREFIX)) {

            String token = requestHeader.substring(JwtProperties.TOKEN_PREFIX.length());
            if (!jwtUtil.validateToken(token)) {
                throw new RuntimeException("[권한 없음] 유효하지 않은 토큰입니다...");
            }
            Claims claims = jwtUtil.findClaims(token);
            String username = String.valueOf(claims.getSubject());

            if (username != null) {
                System.out.println("username = " + username);
                Member member = memberService.findByUsername(username);

                PrincipalDetails principalDetails = new PrincipalDetails(member);
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}

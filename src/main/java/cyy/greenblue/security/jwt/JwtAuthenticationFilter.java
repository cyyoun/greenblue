package cyy.greenblue.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import cyy.greenblue.dto.MemberDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println(" 로그인 시도중 💦 JwtAuthenticationFilter.attemptAuthentication");
        MemberDto member = null;
        //1. username, password 받기
        try {
            ObjectMapper om = new ObjectMapper();
            member = om.readValue(request.getInputStream(), MemberDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        member.getUsername(),
                        member.getPassword());

        setDetails(request, authenticationToken);
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String[] tokens = jwtUtil.createTokenWhenLogin(authResult);
        response.addHeader(JwtProperties.HEADER, JwtProperties.TOKEN_PREFIX + tokens[0]);
        response.addHeader(JwtProperties.CUSTOMIZE_HEADER, JwtProperties.TOKEN_PREFIX + tokens[1]);
    }
}

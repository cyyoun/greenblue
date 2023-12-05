package cyy.greenblue.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import cyy.greenblue.dto.MemberDto;
import cyy.greenblue.security.auth.PrincipalDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

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

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
//        Authentication authentication
//                = getAuthenticationManager().authenticate(authenticationToken);

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("Authentication : " + principalDetails.getMember().getUsername());
        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String[] tokens = jwtUtil.createTokenWhenLogin(authResult);
        response.addHeader(JwtProperties.AUTHORITIES_KEY, tokens[0]);
        response.addHeader(JwtProperties.CUSTOMIZE_HEADER, tokens[1]);
        super.successfulAuthentication(request, response, chain, authResult);
    }
}

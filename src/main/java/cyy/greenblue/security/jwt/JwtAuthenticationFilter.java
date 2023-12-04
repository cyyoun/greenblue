package cyy.greenblue.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import cyy.greenblue.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        System.out.println(" 로그인 시도중 💦 JwtAuthenticationFilter.attemptAuthentication");

        //1. username, password 받기
        try {
            ObjectMapper om = new ObjectMapper();
            Member member = om.readValue(request.getInputStream(), Member.class);
            System.out.println(member);


            /**
             * UsernamePasswordAuthenticationToken 객체를 만들고,
             * 이 객체를 Spring Security의 AuthenticationManager에 전달하여 인증을 시도합니다.
             * 인증이 성공하면 해당 사용자의 Authentication 객체가 생성되고, 
             * 이후 요청에서 사용자의 권한을 확인하는 데 사용됩니다.
             */
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(member.getUsername(), member.getPassword());

            Authentication authenticate = authenticationManager.authenticate(authenticationToken);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //2. 정상인지 확인 (PrincipalDetailsService - loadUserByUsername() 호출)

        //3. PrincipalDetails 세션에 담기 (권한 관리 위함)

        //4. JWT 토큰 생성 후 응답
        return super.attemptAuthentication(request, response);
    }
}

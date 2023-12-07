package cyy.greenblue.security.jwt;

import cyy.greenblue.domain.JwtToken;
import cyy.greenblue.domain.Member;
import cyy.greenblue.service.JwtTokenService;
import cyy.greenblue.service.MemberService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtUtil {
    private final Key key;
    private final MemberService memberService;
    private final JwtTokenService jwtTokenService;

    public JwtUtil(String secret, MemberService memberService, JwtTokenService jwtTokenService) {
        this.memberService = memberService;
        this.jwtTokenService = jwtTokenService;
        byte[] bytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(bytes);
    }

    public String[] createTokenWhenLogin(Authentication authentication) {
        String accessToken = createAccessToken(authentication);
        String refreshToken = createRefreshToken(authentication);
        Member member = findMemberByUsername(authentication.getName());
        JwtToken jwtToken = new JwtToken(member, refreshToken);
        try {
            jwtTokenService.save(jwtToken);
        } catch (Exception e) {
            jwtTokenService.editToken(jwtToken);
        }

        return new String[]{accessToken, refreshToken};
    }

    public String createAccessToken(Authentication authentication) {
        // 토큰 만료 시간 설정 (현재 시간으로부터 30분 후로 설정)
        Instant now = Instant.now();
        Instant expirationTime = now.plusSeconds(60 * 30); // 30분 후
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(JwtProperties.AUTHORITIES_KEY, getAuthorities(authentication))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expirationTime)) // 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS256) // 서명 설정
                .compact();
    }

    public String createRefreshToken(Authentication authentication) {
        // 토큰 만료 시간 설정 (현재 시간으로부터 14일 후로 설정)
        Instant now = Instant.now();
        Instant expirationTime = now.plusSeconds(60 * 60 * 24 * 14); // 2주 후
        return Jwts.builder()
                .setSubject(authentication.getName())
                .setExpiration(Date.from(expirationTime)) // 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS256) // 서명 설정
                .compact();
    }

    public String getAuthorities(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        return authorities;
    }

    public String recreateAccessToken(Authentication authentication) {
        Member member = findMemberByUsername(authentication.getName());
        String refreshToken = jwtTokenService.findTokenByMember(member);
        if (validateToken(refreshToken)) {
            return createAccessToken(authentication);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            findClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("잘못된 jwt 서명입니다.", e);
        } catch (ExpiredJwtException e) {
            log.error("만료된 jwt 토큰입니다.", e);
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 jwt 토큰입니다.", e);
        } catch (IllegalArgumentException e) {
            log.error("jwt 토큰이 잘못되었습니다.", e);
        }
        return false;
    }

    public Claims findClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Member findMemberByUsername(String username) {
        return memberService.findByUsername(username);
    }
}

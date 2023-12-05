package cyy.greenblue.security.jwt;

public interface JwtProperties {
    String SECRET = "나만알고나만아는보안비밀비밀시크릿키";
    int ACCESS_EXPIRATION_TIME = 60 * 30; // 30분 (초)
    int REFRESH_EXPIRATION_TIME = 60 * 60 * 24 * 14; // 14일 (초)
    String TOKEN_PREFIX = "Bearer ";
    String HEADER = "Authorization";
    String CUSTOMIZE_HEADER = "Refresh-Token";
    String AUTHORITIES_KEY = "auth";
}
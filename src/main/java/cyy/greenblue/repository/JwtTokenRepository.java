package cyy.greenblue.repository;

import cyy.greenblue.domain.JwtToken;
import cyy.greenblue.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {
    Optional<JwtToken> findByMember(Member member);

    @Query("SELECT member FROM JwtToken t WHERE t.token = :token")
    Member findMemberByToken(String token);
}

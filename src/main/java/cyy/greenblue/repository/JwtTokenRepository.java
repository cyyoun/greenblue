package cyy.greenblue.repository;

import cyy.greenblue.domain.JwtToken;
import cyy.greenblue.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {
    @Query("SELECT j FROM JwtToken j WHERE j.member = :member")
    JwtToken findByMember(Member member);

}

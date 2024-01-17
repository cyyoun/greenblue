package cyy.greenblue.repository;

import cyy.greenblue.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByUsername(String username); //Jpa Query methods 문법

    @Query("SELECT count(*) FROM Member m WHERE m.username = :username")
    Long countByUsername(String username);

    @Query("SELECT count(*) FROM Member m WHERE m.email = :email")
    Long countByEmail(String email);
}

package cyy.greenblue.repository;

import cyy.greenblue.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    //select * from member where username = 1?
    Member findByUsername(String username); //Jpa Query methods 문법
}

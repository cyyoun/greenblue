package cyy.greenblue.repository;

import cyy.greenblue.domain.Member;
import cyy.greenblue.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PointRepository extends JpaRepository<Point, Long> {
    @Query("SELECT SUM(p.points) FROM Point p WHERE p.member = :member")
    int findPointByMemberId(Member member);

    List<Point> findByMemberId(long memberId);

}

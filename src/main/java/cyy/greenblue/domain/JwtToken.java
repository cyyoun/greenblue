package cyy.greenblue.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class JwtToken {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    private String token;

    public JwtToken(Member member, String token) {
        this.member = member;
        this.token = token;
    }

    public JwtToken updateToken(String token) {
        this.token = token;
        return this;
    }
}

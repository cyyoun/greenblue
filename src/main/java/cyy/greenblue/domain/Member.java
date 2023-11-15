package cyy.greenblue.domain;

import lombok.Builder;
import lombok.Data;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private long id;
    private String username;
    private String password;
    private String email;
    private String role;
    private Grade grade;
    private LocalDateTime regDate;

    private String provider; //ex) google
    private String providerId; //ex) attribute sub 정보

    @Builder
    public Member(String username, String password, String email, String role, String provider, String providerId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }
}

package cyy.greenblue.domain;

import lombok.Builder;
import lombok.Data;

import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberId")
    private long id;
    private String username;
    private String password;
    private String email;
    private String role;
    private Grade grade;

    private String provider; //ex) google
    private String providerId; //ex) attribute sub 정보

    @CreationTimestamp
    private Timestamp createDate;

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

package cyy.greenblue.domain;

import lombok.Builder;

import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Column(name = "provider_id")
    private String providerId; //ex) attribute sub 정보
    private String provider; //ex) google

    @Builder
    public static Member create(String username, String password, String email, String role, String provider, String providerId) {
        return Member.builder()
                .username(username)
                .password(password)
                .email(email)
                .role(role)
                .provider(provider)
                .providerId(providerId)
                .build();
    }

    public Member() {
        this.grade = Grade.BRONZE;
    }

    public void updateGrade(Grade grade) {
        this.grade = grade;
    }
}

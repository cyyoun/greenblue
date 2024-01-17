package cyy.greenblue.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;

import lombok.Data;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    public void updateGrade(Grade grade) {
        this.grade = grade;
    }
}

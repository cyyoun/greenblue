package cyy.greenblue.domain;

import lombok.Data;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberId")
    private long id;
    private String username;
    private String password;
    private String email;
    private String role;

    @CreationTimestamp
    private Timestamp createDate;

}

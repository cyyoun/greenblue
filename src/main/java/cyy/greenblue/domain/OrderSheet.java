package cyy.greenblue.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class OrderSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_sheet_id")
    private Long id;
    private String status;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    public OrderSheet() {
    }

    public OrderSheet(String status) {
        this.status = status;
        this.regDate = LocalDateTime.now();
    }

    public void updateStatus(String status) {
        this.status = status;
    }
}

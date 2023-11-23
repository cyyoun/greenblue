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

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    public OrderSheet() {
    }

    public OrderSheet(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        this.regDate = LocalDateTime.now();
    }

    public void updateStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}

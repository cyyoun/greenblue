package cyy.greenblue.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class OrderSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_sheet_id")
    private long id;
    private String paymentStatus;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @OneToMany(mappedBy = "orderSheet")
    private List<OrderProduct> orderProducts = new ArrayList<>();

    public OrderSheet() {
        this.regDate = LocalDateTime.now();
    }

}

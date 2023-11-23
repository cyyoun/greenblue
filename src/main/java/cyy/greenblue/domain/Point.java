package cyy.greenblue.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Long id;

    private int point;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_product_id")
    private OrderProduct orderProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Point() {
    }

    //리뷰 건에 대한 적립
    public Point(Review review, Member member) {
        this.point = 500;
        this.review = review;
        this.member = member;
    }

    //주문 건에 대한 적립
    public Point(int point, OrderProduct orderProduct, Member member) {
        this.point = point;
        this.orderProduct = orderProduct;
        this.member = member;
    }
}

package cyy.greenblue.domain;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;


    /** 양방향 고려해보기
    @OneToMany(mappedBy = "cart")
    private List<CartProduct> cartProducts = new ArrayList<>();*/

    public Cart() {}

}

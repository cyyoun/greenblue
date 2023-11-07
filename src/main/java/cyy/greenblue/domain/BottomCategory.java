package cyy.greenblue.domain;

import javax.persistence.*;

@Entity
public class BottomCategory {
    @Id
    @Column(name = "bottom_category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "top_category_id")
    private TopCategory topCategory; //단방향 fk

}


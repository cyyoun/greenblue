package cyy.greenblue.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class LeafCategory {
    @Id
    @Column(name = "leaf_category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category; //단방향 fk

}


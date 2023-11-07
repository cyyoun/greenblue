package cyy.greenblue.domain;

import javax.persistence.*;

@Entity
public class TopCategory {

    @Id
    @Column(name = "top_category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
}

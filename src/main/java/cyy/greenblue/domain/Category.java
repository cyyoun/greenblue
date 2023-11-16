package cyy.greenblue.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Category {

    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int depth = 1;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> subCategories = new ArrayList<>();

    public void addDepth(int depth) {
        this.depth = depth;
    }
    public void editName(String name) {
        this.name = name;
    }

}
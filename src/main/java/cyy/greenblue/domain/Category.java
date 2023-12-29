package cyy.greenblue.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class Category {

    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private int depth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> subCategories;

    public Category() {
        this.depth = 1;
        this.subCategories = new ArrayList<>();
    }

    public void updateDepth(int depth) {
        this.depth = depth;
    }
    public void updateName(String name) {
        this.name = name;
    }

}

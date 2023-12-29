package cyy.greenblue.dto;

import cyy.greenblue.domain.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryDto {
    private Integer id;
    private String name;

    @Builder
    public CategoryDto(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public CategoryDto toDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}

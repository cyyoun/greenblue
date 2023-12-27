package cyy.greenblue.dto;

import cyy.greenblue.domain.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryDto {
    private long categoryId;
    private String name;

    @Builder
    public CategoryDto(long categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    public CategoryDto toDto(Category category) {
        return CategoryDto.builder()
                .categoryId(category.getId())
                .name(category.getName())
                .build();
    }
}

package cyy.greenblue.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
public class ProductExtendDto extends ProductOutputDto {
    @NotBlank
    private String description;
    @NotNull
    private LocalDateTime regDate;
    @NotNull
    private CategoryDto categoryDto;
}

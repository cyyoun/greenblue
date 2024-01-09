package cyy.greenblue.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartInputDto {
    private Long id;
    @Range(min = 1, max = 999)
    private int quantity;
    @NotNull
    private Long productId;
}

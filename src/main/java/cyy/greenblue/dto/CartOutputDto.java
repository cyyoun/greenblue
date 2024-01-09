package cyy.greenblue.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartOutputDto {
    @NotNull
    private Long id;
    @NotNull
    private int quantity;
    @NotNull
    private ProductOutputDto productOutputDto;
}

package cyy.greenblue.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OrderProductInputDto {
    private Long id;
    @NotNull
    @Min(1)
    private Long productId;
    @NotNull
    @Min(1)
    private int quantity;

}

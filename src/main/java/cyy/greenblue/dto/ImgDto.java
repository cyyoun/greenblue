package cyy.greenblue.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public abstract class ImgDto {

    private Long id;
    private String filename;

    public ImgDto(Long id, String filename) {
        this.id = id;
        this.filename = filename;
    }

    public abstract ImgDto toDto(Object img);
}

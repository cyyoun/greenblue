package cyy.greenblue.dto;

import cyy.greenblue.domain.Grade;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SimpleMemberInfoDto {
    private String username;
    private Grade grade;
}

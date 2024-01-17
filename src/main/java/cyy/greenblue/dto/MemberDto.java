package cyy.greenblue.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class MemberDto {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    @Email
    private String email;
}

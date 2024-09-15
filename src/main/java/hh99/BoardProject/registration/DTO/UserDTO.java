package hh99.BoardProject.registration.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class UserDTO {

    @NotBlank
    @Pattern(regexp = "[0-9a-z]+", message="Username Must Contain Only Lowered letters and Numbers")
    @Size(min=4, max=10, message="size 4~10")
    private final String userName;

    @NotBlank
    @Pattern(regexp = "[0-9a-zA-Z]+", message="Username Must Contain Only Letters and Numbers")
    @Size(min=8, max=15, message="size 8~15")
    private final String password;

}

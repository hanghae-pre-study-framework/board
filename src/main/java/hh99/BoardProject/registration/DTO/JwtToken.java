package hh99.BoardProject.registration.DTO;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class JwtToken {
    private String grantType; //JWT에 대한 인증타입
    private String accessToken;
    private String refreshToken;
}

package hh99.BoardProject.registration.userService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenMakerService {
    /**
     *  토큰 발급받기
     *  1. 사용자가 로그인 정보를 서버에 전송.
     *  2. 서버는 로그인 정보가 유효하면 JWT토큰을 생성해서 반환
     *  3. 클라이언트는 서버로부터 받은 JWT토큰을 Authoriztion헤더에 포함하여 이후의 요청을 보냄
     *  4. 서버는 각 요청에서 JWT토큰을 검증해서 인증 처리.
     *  */
    private final long accessTokenExpMilliseconds = 3600000;
    private int refreshTokenExpMinutes = 100;

    private SecretKey secretKey;

    public TokenMakerService(@Value("${spring.jwt.secret}") String secret){ //Window에서 설정 방법 : setx JWT_SECRET "시크릿 키" / Linux&maxOS :  export JWT_SECRET="시크릿키"
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
//void가 있어서 생성자가아닌 매서드로 인식
    public String createToken(String username){
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenExpMilliseconds);

        return Jwts.builder()
                .setSubject(username) //주체설정
                .setIssuedAt(now)//토큰 발행 시간
                .setExpiration(validity) // 토큰 만료 시간
                .signWith(secretKey) // 비밀키로 서명
                .compact(); //JWT생성

    }
    public String createToken2(Authentication authentication){
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenExpMilliseconds);

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return "Bearer " + Jwts.builder()
                .setSubject(authentication.getName()) //주체설정
                .setIssuedAt(now)//토큰 발행 시간
                .setExpiration(validity) // 토큰 만료 시간
                .signWith(secretKey) // 비밀키로 서명
                .compact(); //JWT생성
    }

    // JWT 토큰에서 사용자 이름(Subject) 가져오기
    public String getUsername(String token) {
        String username = Jwts.parserBuilder()  // 최신 버전에서는 parserBuilder() 사용
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        log.debug("your token : {} / your name : {}",token,username);
        return username;  // JWT의 주체(사용자 이름) 반환
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            log.info("This token is verified : {}",token);
            return true;
        }catch(Exception e){
            log.error("Invalid Token {}",token);
            return false;
        }
    }
}

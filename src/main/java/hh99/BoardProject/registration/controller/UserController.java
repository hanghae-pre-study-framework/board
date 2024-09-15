package hh99.BoardProject.registration.controller;

import hh99.BoardProject.registration.DTO.JwtResponse;
import hh99.BoardProject.registration.DTO.LoginDTO;
import hh99.BoardProject.registration.DTO.UserDTO;
import hh99.BoardProject.registration.entity.UserInfo;
import hh99.BoardProject.registration.userService.TokenMakerService;
import hh99.BoardProject.registration.userService.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenMakerService tokenMakerService;
    private final AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserDTO user){
        log.info("Registering user: {}", user.getUserName());
        if (userService.registerUser(user).equals("REQUEST_SUCCESS")) {
            log.info("User registered successfully: {}", user.getUserName());
            return ResponseEntity.ok( "User registered successfully!");
        } else {
            log.warn("Username already exists: {}", user.getUserName());
            return ResponseEntity.badRequest().body("Username "+user.getUserName()+" already exists!");
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody LoginDTO loginInfo, HttpServletResponse response){
        log.info("Login attempt for user: {}", loginInfo.getUserName());
        try {
            //세션 등록
            Authentication auth = authenticationManager.authenticate(
                    /**Spring Security는 기본적으로 세션 쿠키 방식의 인증이 이루어짐. 인증이 이루어지는 필터.
                     * AbstractAuthenticationProcessingFilter 를 상속받고있다.
                     * https://cjw-awdsd.tistory.com/45
                     * **/
                    new UsernamePasswordAuthenticationToken(loginInfo.getUserName(), loginInfo.getPassword()));
            //AuthenticationManager : 인증 논리를 구현하기 위해 인증 공급자를 이용하여 인증 처리

            SecurityContextHolder.getContext().setAuthentication(auth);

            String jwtToken = tokenMakerService.createToken2(auth);
            log.info("Login Success for user: {}", loginInfo.getUserName());
            return ResponseEntity.ok(new JwtResponse(jwtToken));
        }
        catch (Exception e){
            log.warn("Invalid username or password for user: {}", loginInfo.getUserName());
            return ResponseEntity.status(401).body("Invalid username or password");
        }
        //response.setHeader("Authorization", "Bearer" + jwtToken);
        //사용자 인증 처리
        /*if(userService.validateUser(loginInfo.getUserName(), loginInfo.getPassword())){
            String token = tokenMakerService.createToken(loginInfo.getUserName());
            log.info("Login Success");
            return ResponseEntity.ok(new JwtResponse(token));
        }else{
            log.warn("Invalid username or password for user: {}", loginInfo.getUserName());
            return ResponseEntity.status(401).body("Invalid username or password");
        }*/

    }

}
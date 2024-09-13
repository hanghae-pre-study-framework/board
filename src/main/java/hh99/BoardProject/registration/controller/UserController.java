package hh99.BoardProject.registration.controller;

import hh99.BoardProject.registration.DTO.JwtResponse;
import hh99.BoardProject.registration.entity.UserInfo;
import hh99.BoardProject.registration.userService.TokenMakerService;
import hh99.BoardProject.registration.userService.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private TokenMakerService tokenMakerService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserInfo user){
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
    public ResponseEntity<?> userLogin(@RequestBody UserInfo user){
        log.info("Login attempt for user: {}", user.getUserName());

        //사용자 인증 처리
        if(userService.validateUser(user.getUserName(), user.getPassword())){
            String token = tokenMakerService.createToken(user.getUserName());
            log.info("Login Success");
            return ResponseEntity.ok(new JwtResponse(token));
        }else{
            log.warn("Invalid username or password for user: {}", user.getUserName());
            return ResponseEntity.status(401).body("Invalid username or password");
        }

    }

}
package hh99.BoardProject.registration.controller;

import hh99.BoardProject.registration.entity.UserInfo;
import hh99.BoardProject.registration.userService.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserInfo user){
        if (userService.registerUser(user).equals("REQUEST_SUCCESS")) {
            return ResponseEntity.ok( "User registered successfully!");
        } else {
            return ResponseEntity.badRequest().body("Username "+user.getUserName()+" already exists!");
        }
    }
}
package hh99.BoardProject.registration.controller;

import hh99.BoardProject.registration.entity.UserInfo;
import hh99.BoardProject.registration.userService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    //@PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserInfo user){
        if (userService.registerUser(user)) {
            return ResponseEntity.ok("User registered successfully!");
        } else {
            return ResponseEntity.badRequest().body("Username "+user.getUserName()+" already exists!");
        }
    }
}
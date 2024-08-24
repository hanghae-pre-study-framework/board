package registration.controller;

import registration.entity.UserInfo;
import registration.userService.GetUserInfo;
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
    private GetUserInfo userInfo;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserInfo user){
        if (userInfo.registerUser(user)) {
            return ResponseEntity.ok("User registered successfully!");
        } else {
            return ResponseEntity.badRequest().body("Username "+user.getUserName()+" already exists!");
        }
    }
}
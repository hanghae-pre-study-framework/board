package hh99.BoardProject.registration.userService;

import hh99.BoardProject.registration.entity.UserInfo;
import hh99.BoardProject.registration.repository.UserInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;


@Service
@Slf4j
public class UserService {


    private final UserInfoRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserInfoRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String registerUser(UserInfo user){
        String clientIp = "";
    //    String encodedPassword = passwordEncoder.encode(user.getPassword());
        log.info("User registration started for: {}", user.getUserName());
        log.info("Login attempt for user: {}", user.getUserName());
        if(userRepository.findOneByUserName(user.getUserName()) != null)
            return "EXISTS_USER";

        DateFormat now = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        user.setRegDate(now.format(new Date()));
        user.setRegIp(clientIp);
    //    user.setPassword(encodedPassword);

        userRepository.save(user);
        return "REQUEST_SUCCESS"; //회원가입 성공
        //비밀번호 암호화 한 뒤 전송 > 비교
    }

    public boolean validateUser(String username, String rawPassword) {
        Optional<UserInfo> optionalUser = userRepository.findByUserName(username);
        if (optionalUser.isPresent()) {
            UserInfo user = optionalUser.get();
            log.info("User found: {}", username);
            log.info("Comparing passwords: rawPassword = {}, storedPassword = {}", rawPassword, user.getPassword());
            return rawPassword.equals(user.getPassword());  // 평문 비밀번호 비교
        } else {
            log.warn("User not found: {}", username);
            return false;
        }
    }
}

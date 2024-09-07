package hh99.BoardProject.registration.userService;

import hh99.BoardProject.registration.entity.UserInfo;
import hh99.BoardProject.registration.repository.UserInfoRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


@Service
public class UserService {


    private final UserInfoRepository userRepository;

    public UserService(UserInfoRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String registerUser(UserInfo user){
        String clientIp = "";
    //    UserInfo user = new UserInfo();

        if(userRepository.findOneByUserName(user.getUserName()) != null)
            return "EXISTS_USER";

        DateFormat now = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

        user.setRegDate(now.format(new Date()));

        user.setRegIp(clientIp);

        userRepository.save(user);
        return "REQUEST_SUCCESS"; //회원가입 성공

    }

}

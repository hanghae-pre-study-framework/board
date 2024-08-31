package hh99.BoardProject.registration.userService;

import hh99.BoardProject.registration.entity.UserInfo;
import hh99.BoardProject.registration.repository.UserInfoRepository;
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

    public boolean registerUser(UserInfo user){

    //    UserInfo user = new UserInfo();

        if(userRepository.findOneByUserName(user.getUserName()) != null)
            return false;
//        if(userRepository.findOneByUserName(user.getUserName()))
//            return false;
        DateFormat now = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

        user.setRegDate(now.format(new Date()));

        user.setRegIp("");

        userRepository.save(user);
        return true; //회원가입 성공

    }
    //사용되지않는 게시글을 삭제하는경우
    //비밀번호를 잘못 입력한 경우
    //없는게시글 삭제하는경우

}

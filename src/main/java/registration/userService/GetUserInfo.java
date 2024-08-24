package registration.userService;

import registration.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import registration.repository.UserInfoRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


@Service
public class GetUserInfo {

    @Autowired
    private UserInfoRepository userRepository;

    public boolean registerUser(UserInfo user){

    //    UserInfo user = new UserInfo();

        if(userRepository.findOneByUserName(user.getUserName()) != null)
            return false;
//        if(userRepository.findOneByUserName(user.getUserName()))
//            return false;
        DateFormat now = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

        user.setUserName(user.getUserName());
        user.setPassword(user.getPassword());
        user.setRegId("registerUser");
        user.setRegDate(now.format(new Date()));

        user.setRegIp("");

        userRepository.save(user);
        return true; //회원가입 성공

    }
/*
    public UserInfo registerUser(String user_name, String password, String reg_id, String reg_ip){
        if(userRepository.existsByUserName(user_name)){
            throw new IllegalStateException(user_name+" is already exists.");
        }

        SimpleDateFormat now = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss");

        UserInfo user = new UserInfo();

        user.setUser_name(user_name);
        user.setPassword(password);
        user.setReg_id("registerUser");
        user.setReg_date(now.format(new Date()));
        user.setReg_ip("");

        return userRepository.save(user);
    }

 */
}

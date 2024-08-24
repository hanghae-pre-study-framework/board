package hh99.BoardProject.registration.userService;

import hh99.BoardProject.registration.entity.BoardList;
import hh99.BoardProject.registration.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hh99.BoardProject.registration.repository.BoardListRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class BoardService {

    @Autowired
    BoardListRepository boardListRepository;
    UserInfoRepository userInfoRepository;

    public Integer registPost(BoardList list)
    {
        DateFormat now = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

        //if(userInfoRepository.findOneByUserName(list.getUserName()) == null)
        //    //존재하지 않는 유저
       //     return -10;

        if(list.getPassword() == null || list.getPassword().isEmpty()){
            //패스워드 누락
            return -5;
        }
        else{
            list.setRegDate(now.format(new Date()));
            list.setUseYn("Y");

            boardListRepository.save(list);
            return 0;
        }
    }
}

package hh99.BoardProject.registration.userService;

import hh99.BoardProject.registration.entity.BoardList;
import hh99.BoardProject.registration.projection.PostSummaryProjection;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import hh99.BoardProject.registration.repository.BoardListRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class BoardService {



    private final BoardListRepository boardListRepository;

    public BoardService(BoardListRepository boardListRepository) {
        this.boardListRepository = boardListRepository;
    }


    public Integer registPost(BoardList post)
    {
        DateFormat now = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

        //if(userInfoRepository.findOneByUserName(list.getUserName()) == null)
        //    //존재하지 않는 유저
       //     return -10;

        if(post.getPassword() == null || post.getPassword().isEmpty()){
            //패스워드 누락
            return -5;
        }
        else{
            post.setRegDate(now.format(new Date()));
            post.setUseYn("Y");

            boardListRepository.save(post);
            return 0;
        }
    }

    public List<BoardList> getAllPosts(){
        return boardListRepository.findAll(Sort.by(Sort.Direction.DESC,"regDate"));

    }
    public List<BoardList> getAllPosts2(){
        return boardListRepository.findPostSummaries();
    }

    public List<PostSummaryProjection> getAllPost3(){
        return boardListRepository.findAllBy(Sort.by(Sort.Direction.DESC, "regDate"));
    }
}

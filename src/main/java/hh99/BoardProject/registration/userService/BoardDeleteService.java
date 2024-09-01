package hh99.BoardProject.registration.userService;

import hh99.BoardProject.registration.entity.BoardList;
import hh99.BoardProject.registration.repository.BoardListRepository;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
public class BoardDeleteService {
    private final BoardListRepository boardListRepository;

    public BoardDeleteService(BoardListRepository boardListRepository) {
        this.boardListRepository = boardListRepository;
    }

    public boolean DeletePost(BoardList post, Integer seq_no){ //soft delete
        DateFormat now = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

        Optional<BoardList> originPost = boardListRepository.findBySeqNo(seq_no);
        //BoardList originPost = boardListRepository.findBySeqNo(seq_no);

        if(originPost.isPresent() && originPost.stream().allMatch(s->s.getUseYn().equals("Y")))
            if(originPost.stream().allMatch(s-> CheckPassword(s.getPassword(), post.getPassword()))){
                BoardList p = originPost.get();
                p.setUseYn("N");
                p.setEditId(post.getEditId());
                p.setEditDate(now.format(new Date()));

                boardListRepository.save(p);
                return true;
            }
        else{
            return false;
        }
    //   if(CheckPassword(post.getPassword(), password)) {
    //       post.setSeqNo(seq_no);
    //       post.setEditDate(now.format(new Date()));
    //       post.setEditId(post.getEditId());
    //       post.setUseYn("N");

    //       boardListRepository.save();
    //       return true;
    //   }
    //   else
    //       return false;
        return false;
    }

    private Boolean CheckPassword(String delete_password, String password){
        return (delete_password.equals(password));
    }
}

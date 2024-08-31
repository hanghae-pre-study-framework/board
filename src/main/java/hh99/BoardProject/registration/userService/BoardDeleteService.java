package hh99.BoardProject.registration.userService;

import hh99.BoardProject.registration.entity.BoardList;
import hh99.BoardProject.registration.repository.BoardListRepository;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class BoardDeleteService {
    private final BoardListRepository boardListRepository;

    public BoardDeleteService(BoardListRepository boardListRepository) {
        this.boardListRepository = boardListRepository;
    }

    public BoardList DeletePost(BoardList post, Integer seq_no){ //soft delete
        DateFormat now = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

        String password = boardListRepository.findBySeqNo(seq_no).getPassword();

        if(CheckPassword(post.getPassword(), password)) {
            post.setSeqNo(seq_no);
            post.setEditDate(now.format(new Date()));
            post.setEditId(post.getEditId());
            post.setUseYn("N");

            return boardListRepository.save(post);
        }
        else
            return null;
    }

    private Boolean CheckPassword(String delete_password, String password){
        return (delete_password.equals(password));
    }
}

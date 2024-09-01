package hh99.BoardProject.registration.userService;

import hh99.BoardProject.registration.entity.BoardList;
import hh99.BoardProject.registration.projection.PostSummaryProjection;
import hh99.BoardProject.registration.repository.BoardListRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class BoardWriteService {



    private final BoardListRepository boardListRepository;

    public BoardWriteService(BoardListRepository boardListRepository) {
        this.boardListRepository = boardListRepository;
    }

    /***
     * 게시글 등록
     * 작성 내용 저장 후 게시글 Client로 반환(게시물 번호 반환하기)
     */

    public BoardList registPost(BoardList post)
    {
        DateFormat now = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

        post.setRegDate(now.format(new Date()));
        post.setUseYn("Y");


        return boardListRepository.save(post);
    }


    /**
     * 선택한 게시글 수정.
     * 수정 요청 시 수정할 데이터와 비밀번호 같이 보내서 서버에서 비밀번호 일치여부 확인 후
     * 제목, 작성자명, 작성내용을 수정하고 수정된 게시글 Client로 반환
     * */

    public BoardList editPost(BoardList post, Integer seq_no){
        DateFormat now = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

        String password = boardListRepository.findBySeqNo(seq_no).get().getPassword();

        if(CheckPassword(post.getPassword(), password)) {
            post.setSeqNo(seq_no);
            post.setEditDate(now.format(new Date()));
            post.setEditId(post.getEditId());
            post.setUseYn("Y");

            return boardListRepository.save(post);
        }
        else
            return null;
    }

    private Boolean CheckPassword(String edit_password, String password){
        return (edit_password.equals(password));
    }
}

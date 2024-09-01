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
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class BoardService {



    private final BoardListRepository boardListRepository;

    public BoardService(BoardListRepository boardListRepository) {
        this.boardListRepository = boardListRepository;
    }

    /**
    * 게시글 전체 조회
    * */
    public List<BoardList> getAllPosts(){
        return boardListRepository.findAll(Sort.by(Sort.Direction.DESC,"regDate"));

    }
    public List<BoardList> getAllPosts2(){
        return boardListRepository.findPostSummaries();
    }

    public List<PostSummaryProjection> getAllPost3(){
        return boardListRepository.findAllByUseYn("Y", Sort.by(Sort.Direction.DESC, "regDate"));
    }

    /**
     * 게시글 단건 조회
     * */
    public BoardList getPost(Integer seq_no){
        Optional<BoardList> boardListOptional = boardListRepository.findBySeqNo(seq_no);
        return boardListOptional.orElseThrow(()-> new NoSuchElementException("Post not found"));
    }
}

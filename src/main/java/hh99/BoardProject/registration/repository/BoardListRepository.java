package hh99.BoardProject.registration.repository;

import hh99.BoardProject.registration.entity.BoardList;
import hh99.BoardProject.registration.projection.PostSummaryProjection;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardListRepository extends JpaRepository<BoardList, String> {

    //DTO로 select하는 방법
    @Query("SELECT new hh99.BoardProject.registration.entity.BoardList( p.title, p.regId, p.contents, p.regDate) FROM BoardList p ORDER BY p.regDate DESC")
    List<BoardList> findPostSummaries();

    //Projection이용해서 하는 방법
    List<PostSummaryProjection> findAllBy(Sort regDate);

    BoardList findBySeqNo(Integer seqNo);
}

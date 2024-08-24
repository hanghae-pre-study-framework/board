package hh99.BoardProject.registration.repository;

import hh99.BoardProject.registration.entity.BoardList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardListRepository extends JpaRepository<BoardList, String> {
    BoardList findOneByUserName(String userName); //findOne 하나 find : array

}
